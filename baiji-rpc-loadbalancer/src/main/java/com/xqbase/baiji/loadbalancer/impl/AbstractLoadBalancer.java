package com.xqbase.baiji.loadbalancer.impl;

import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.Server;

/**
 * AbstractLoadBalancer contains features required for most loadbalancing
 * implementations.
 *
 * An anatomy of a typical LoadBalancer consists of 1. A List of Servers (nodes)
 * that are potentially bucketed based on a specific criteria. 2. A Class that
 * defines and implements a LoadBalacing Strategy via <code>IRule</code> 3. A
 * Class that defines and implements a mechanism to determine the
 * suitability/availability of the nodes/servers in the List.
 *
 * @author Tony He
 *
 */
public abstract class AbstractLoadBalancer implements LoadBalancer {

    /**
     * Delegate to {@link #choose(Object)} with parameter null.
     */
    public Server choose() {
        return choose(null);
    }
}
