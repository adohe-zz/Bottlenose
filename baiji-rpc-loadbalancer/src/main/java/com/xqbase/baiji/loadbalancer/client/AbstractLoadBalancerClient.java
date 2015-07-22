package com.xqbase.baiji.loadbalancer.client;

import com.xqbase.baiji.client.ServiceClient;
import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.context.LoadBalancerContext;

/**
 * Abstract class that provides the integration of client with load balancers.
 *
 * @author Tony He
 *
 */
public abstract class AbstractLoadBalancerClient extends LoadBalancerContext implements ServiceClient {

    public AbstractLoadBalancerClient(LoadBalancer lb) {
        super(lb);
    }
}
