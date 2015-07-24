package com.xqbase.baiji.loadbalancer.reactive;

import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.Server;
import com.xqbase.baiji.loadbalancer.context.LoadBalancerContext;
import com.xqbase.baiji.loadbalancer.stats.ServerStats;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

import java.net.URI;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * A command that is used to produce the Observable from the load balancer execution. The load balancer is responsible for
 * the following:
 *
 * @author Tony He
 */
public class LoadBalancerCommand<T> {

    public static class Builder<T> {

        private LoadBalancer loadBalancer;
        private LoadBalancerContext loadBalancerContext;
        private List<? extends ExecutionListener<?, T>> listeners;
        private ExecutionContextListenerInvoker<?, T> invoker;
        private ExecutionContext<?> executionContext;
        private Object loadBalancerKey;
        private URI loadBalancerURI;
        private Server server;

        private Builder() {
        }

        public Builder<T> withLoadBalancer(LoadBalancer loadBalancer) {
            this.loadBalancer = loadBalancer;
            return this;
        }

        public Builder<T> withLoadBalancerContext(LoadBalancerContext loadBalancerContext) {
            this.loadBalancerContext = loadBalancerContext;
            return this;
        }

        public Builder<T> withLoadBalancerKey(Object loadBalancerKey) {
            this.loadBalancerKey = loadBalancerKey;
            return this;
        }

        public Builder<T> withLoadBalancerURI(URI loadBalancerURI) {
            this.loadBalancerURI = loadBalancerURI;
            return this;
        }

        public Builder<T> withServer(Server server) {
            this.server = server;
            return this;
        }

        public Builder<T> withExecutionContext(ExecutionContext<?> executionContext) {
            this.executionContext = executionContext;
            return this;
        }

        @SuppressWarnings("unchecked")
        public Builder<T> withListeners(List<? extends ExecutionListener<?, T>> listeners) {
            if (null == this.listeners) {
                this.listeners = new LinkedList<>(listeners);
            } else {
                this.listeners.addAll((Collection) listeners);
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public LoadBalancerCommand<T> build() {
            if (null == loadBalancerContext && null == loadBalancer) {
                throw new IllegalArgumentException("");
            }

            if (listeners != null && listeners.size() > 0) {
                this.invoker = new ExecutionContextListenerInvoker(this.executionContext, listeners);
            }

            if (null == loadBalancerContext) {
                this.loadBalancerContext = new LoadBalancerContext(loadBalancer);
            }

            return new LoadBalancerCommand<>(this);
        }
    }

    private final Object loadBalancerKey;
    private final URI loadBalancerURI;

    private final LoadBalancerContext loadBalancerContext;
    private final Server server;
    private final ExecutionContextListenerInvoker<?, T> listenerInvoker;
    private volatile ExecutionInfo executionInfo;

    public LoadBalancerCommand(Builder<T> builder) {
        this.loadBalancerContext = builder.loadBalancerContext;
        this.loadBalancerKey = builder.loadBalancerKey;
        this.loadBalancerURI = builder.loadBalancerURI;
        this.listenerInvoker = builder.invoker;
        this.server = builder.server;
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    class ExecutionInfoContext {
        Server server;
        int instanceAttemptCount = 0;
        int attemptCount = 0;

        public void setServer(Server server) {
            this.server = server;
            this.instanceAttemptCount ++;

            this.attemptCount = 0;
        }

        public void incAttemptCount() {
            this.attemptCount ++;
        }

        public int getAttemptCount() {
            return attemptCount;
        }

        public Server getServer() {
            return server;
        }

        public int getInstanceAttemptCount() {
            return instanceAttemptCount;
        }

        public ExecutionInfo toExecutionInfo() {
            return ExecutionInfo.create(server, attemptCount - 1, instanceAttemptCount - 1);
        }

        public ExecutionInfo toFinalExecutionInfo() {
            return ExecutionInfo.create(server, attemptCount, instanceAttemptCount - 1);
        }
    }

    /**
     * Return an Observable that either emits only the single requested server
     * or queries the load balancer for the next server on each subscription
     */
    private Observable<Server> selectServer() {
        return Observable.create(new Observable.OnSubscribe<Server>() {
            @Override
            public void call(Subscriber<? super Server> subscriber) {
                try {
                    Server server = loadBalancerContext.getServerFromLoadBalancer(loadBalancerKey);
                    subscriber.onNext(server);
                    subscriber.onCompleted();
                } catch (Throwable t) {
                    subscriber.onError(t);
                }
            }
        });
    }

    public Observable<T> submit(final ServerOperation<T> operation) {
        final ExecutionInfoContext context = new ExecutionInfoContext();

        if (listenerInvoker != null) {
            try {
                listenerInvoker.onExecutionStart();
            } catch (ExecutionListener.AbortExecutionException e) {
                return Observable.error(e);
            }
        }

        // Use the load balancer
        Observable<T> o =
                ((null == server) ? selectServer() : Observable.just(server))
                .concatMap(new Func1<Server, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(Server server) {
                        context.setServer(server);
                        final ServerStats serverStats = loadBalancerContext.getServerStats(server);

                        // Called for each attempt and retry
                        Observable<Server> o = Observable
                                .just(server)
                                .concatMap(new Func1<Server, Observable<? extends Server>>() {
                                    @Override
                                    public Observable<? extends Server> call(Server server) {
                                        context.incAttemptCount();
                                        loadBalancerContext.noteOpenConnection(serverStats);

                                        if (listenerInvoker != null) {
                                            try {
                                                listenerInvoker.onStartWithInstance(context.toExecutionInfo());
                                            } catch (ExecutionListener.AbortExecutionException e) {
                                                return Observable.error(e);
                                            }
                                        }


                                        return null;
                                    }
                                });
                        return null;
                    }
                });

        return o;
    }
}
