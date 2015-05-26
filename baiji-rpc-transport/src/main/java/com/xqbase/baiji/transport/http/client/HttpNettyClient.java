package com.xqbase.baiji.transport.http.client;

import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.RequestContext;
import com.xqbase.baiji.m2.Response;
import com.xqbase.baiji.transport.apool.AsyncPool;
import com.xqbase.baiji.transport.apool.impl.AsyncPoolImpl;
import com.xqbase.baiji.transport.apool.impl.NoopCreateLatch;
import com.xqbase.baiji.transport.bridge.client.ChannelPoolFactory;
import com.xqbase.baiji.transport.bridge.client.ChannelPoolLifeCycle;
import com.xqbase.baiji.transport.bridge.client.ChannelPoolManager;
import com.xqbase.baiji.transport.bridge.client.TransportClient;
import com.xqbase.baiji.transport.bridge.common.TimeoutTransportCallback;
import com.xqbase.baiji.transport.bridge.common.TransportCallback;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * HttpClient based on Netty.
 *
 * @author Tony He
 */
public class HttpNettyClient implements TransportClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpNettyClient.class);

    private final ChannelPoolManager poolManager;
    private final ChannelGroup allChannels = new DefaultChannelGroup("Transport Channels", GlobalEventExecutor.INSTANCE);

    private final ScheduledExecutorService timeoutSchedule;
    private final ExecutorService callbackExecutor;

    private final int requestTimeout;
    private final String name;

    public HttpNettyClient(ScheduledExecutorService timeoutSchedule,
                           ExecutorService callbackExecutor,
                           int requestTimeout,
                           int idleTimeout,
                           int poolSize,
                           int maxWaiters,
                           String name,
                           int minPoolSize,
                           AsyncPoolImpl.Strategy strategy) {
        this.name = name;
        this.requestTimeout = requestTimeout;
        this.timeoutSchedule = timeoutSchedule;
        this.callbackExecutor = callbackExecutor;
        this.poolManager = new ChannelPoolManager(new ChannelPoolFactoryImpl(
                new Bootstrap(),
                poolSize,
                minPoolSize,
                idleTimeout,
                maxWaiters,
                strategy
        ), name);
    }

    @Override
    public void request(Request request, RequestContext requestContext, TransportCallback<Response> callback) {
    }

    private void writeRequestWithTimeout(Request request, RequestContext requestContext, TransportCallback<Response> callback) {
        TimeoutTransportCallback<Response> timeoutCallback = new TimeoutTransportCallback<>(timeoutSchedule, callbackExecutor,
                requestTimeout, TimeUnit.MICROSECONDS, callback);
    }

    private void writeRequest(Request request, RequestContext requestContext, final TimeoutTransportCallback callback) {

    }

    private class ChannelPoolFactoryImpl implements ChannelPoolFactory {

        private final Bootstrap clientBootstrap;
        private final int maxSize;
        private final int minSize;
        private final long idleTimeout;
        private final int maxWaiterSize;
        private final AsyncPoolImpl.Strategy strategy;

        private ChannelPoolFactoryImpl(Bootstrap clientBootstrap, int maxSize, int minSize,
                    long idleTimeout, int maxWaiterSize, AsyncPoolImpl.Strategy strategy) {
            this.clientBootstrap = clientBootstrap;
            this.maxSize = maxSize;
            this.minSize = minSize;
            this.idleTimeout = idleTimeout;
            this.maxWaiterSize = maxWaiterSize;
            this.strategy = strategy;
        }

        @Override
        public AsyncPool<Channel> getPool(SocketAddress address) {
            return new AsyncPoolImpl<>("Http Channel Pool",
                                                maxSize,
                                                idleTimeout,
                                                new ChannelPoolLifeCycle(
                                                        clientBootstrap,
                                                        address,
                                                        allChannels
                                                ),
                                                timeoutSchedule,
                                                callbackExecutor,
                                                maxWaiterSize,
                                                minSize,
                                                new NoopCreateLatch(),
                                                strategy);
        }
    }
}
