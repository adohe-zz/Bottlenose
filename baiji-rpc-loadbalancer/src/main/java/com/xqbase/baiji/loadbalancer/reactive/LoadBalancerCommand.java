package com.xqbase.baiji.loadbalancer.reactive;

import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.Server;
import com.xqbase.baiji.loadbalancer.context.LoadBalancerContext;

import java.net.URI;
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
        private Object loadBalancerKey;
        private URI loadBalancerURI;
        private Server server;

        private Builder() {
        }
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }
}
