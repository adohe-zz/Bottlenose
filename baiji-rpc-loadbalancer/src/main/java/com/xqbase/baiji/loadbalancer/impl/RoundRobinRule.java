package com.xqbase.baiji.loadbalancer.impl;

import com.xqbase.baiji.loadbalancer.Server;

/**
 * The most well known and basic loadbalacing strategy, i.e. Round Robin Rule.
 *
 * @author Tony He
 */
public class RoundRobinRule extends AbstractLoadBalancerRule {

    @Override
    public Server choose(Object key) {
        return null;
    }
}
