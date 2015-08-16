package com.xqbase.bn.loadbalancer.client;

import com.xqbase.bn.client.ServiceClient;
import com.xqbase.bn.loadbalancer.LoadBalancer;
import com.xqbase.bn.loadbalancer.context.LoadBalancerContext;
import com.xqbase.bn.m2.Request;
import com.xqbase.bn.m2.Response;

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
