package com.xqbase.baiji.loadbalancer.client;

import com.xqbase.baiji.client.ServiceClient;
import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.context.LoadBalancerContext;
import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.Response;

/**
 * Abstract class that provides the integration of client with load balancer.
 *
 * @author Tony He
 *
 */
public abstract class AbstractLoadBalancerAwareClient<S extends Request, T extends Response> extends LoadBalancerContext implements ServiceClient {

    public AbstractLoadBalancerAwareClient(LoadBalancer lb) {
        super(lb);
    }

    public T executeWithLoadBalancer(S request) {
        return null;
    }


}
