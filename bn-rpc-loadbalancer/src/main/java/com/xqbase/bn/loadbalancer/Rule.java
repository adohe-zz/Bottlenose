package com.xqbase.bn.loadbalancer;

/**
 * Interface that defines a "Rule" for a LoadBalancer. A Rule can be thought of
 * as a Strategy for loadbalacing. Well known loadbalancing strategies include
 * Round Robin, Response Time based etc.
 *
 * @author Tony He
 *
 */
public interface Rule {

    /*
     * choose one alive server from lb.allServers or
     * lb.upServers according to key
     *
     * @return chosen Server object. NULL is returned if none
     *  server is available
     */
    Server choose(Object key);

    void setLoadBalancer(LoadBalancer loadBalancer);

    LoadBalancer getLoadBalancer();
}
