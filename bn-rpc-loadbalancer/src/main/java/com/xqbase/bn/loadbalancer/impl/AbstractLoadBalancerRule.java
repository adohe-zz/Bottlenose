package com.xqbase.bn.loadbalancer.impl;

import com.xqbase.bn.loadbalancer.LoadBalancer;
import com.xqbase.bn.loadbalancer.Rule;

/**
 * Class that provides a default implementation for setting and getting load balancer.
 *
 * @author Tony He
 */
public abstract class AbstractLoadBalancerRule implements Rule {

    private LoadBalancer lb;

    @Override
    public void setLoadBalancer(LoadBalancer loadBalancer) {
        this.lb = loadBalancer;
    }

    @Override
    public LoadBalancer getLoadBalancer() {
        return this.lb;
    }
}
