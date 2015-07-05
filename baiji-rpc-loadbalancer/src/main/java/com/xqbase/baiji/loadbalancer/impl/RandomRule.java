package com.xqbase.baiji.loadbalancer.impl;

import com.xqbase.baiji.loadbalancer.Server;

/**
 * A loadbalacing strategy that randomly distributes traffic amongst existing
 * servers.
 *
 * @author Tony He
 */
public class RandomRule extends AbstractLoadBalancerRule {
    
    @Override
    public Server choose(Object key) {
        return null;
    }
}
