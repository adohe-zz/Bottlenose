package com.xqbase.baiji.loadbalancer;

/**
 * Interface that defines how we "ping" a server to check if its alive.
 *
 * @author Tony He
 */
public interface Ping {

    /**
     * Checks whether the given <code>Server</code> is "alive" i.e. should be
     * considered as a candidate while loadbalancing.
     *
     */
    boolean isAlive(Server server);
}
