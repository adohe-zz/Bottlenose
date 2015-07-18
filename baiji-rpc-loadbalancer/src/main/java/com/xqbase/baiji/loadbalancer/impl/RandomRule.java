package com.xqbase.baiji.loadbalancer.impl;

import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.Server;

import java.util.Random;

/**
 * A loadbalacing strategy that randomly distributes traffic amongst existing
 * servers.
 *
 * @author Tony He
 */
public class RandomRule extends AbstractLoadBalancerRule {

    private Random random;

    public RandomRule() {
        this.random = new Random();
    }

    private Server choose(LoadBalancer lb, Object key) {
        return null;
    }

    @Override
    public Server choose(Object key) {
        return null;
    }
}
