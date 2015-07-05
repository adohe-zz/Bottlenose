package com.xqbase.baiji.loadbalancer.stats;

/**
 * Class that acts as a repository of operational charateristics and statistics
 * of every Node/Server in the LaodBalancer.
 *
 * This information can be used to just observe and understand the runtime
 * behavior of the loadbalancer or more importantly for the basis that
 * determines the loadbalacing strategy
 *
 * @author Tony He
 */
public class LoadBalancerStats {

    private String name;

    public LoadBalancerStats(String name) {
        this.name = name;
    }
}
