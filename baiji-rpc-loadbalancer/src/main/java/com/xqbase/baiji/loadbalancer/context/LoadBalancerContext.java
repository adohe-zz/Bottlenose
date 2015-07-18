package com.xqbase.baiji.loadbalancer.context;

import com.xqbase.baiji.client.config.IClientConfig;
import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.Server;

/**
 * A class contains APIs intended to be used be load balancing client which is subclass of this class.
 *
 * @author Tony He
 */
public class LoadBalancerContext implements IClientConfig {

    private static final String DEFAULT_NAME = "default";

    private String name = DEFAULT_NAME;
    private LoadBalancer lb;

    public LoadBalancerContext(String name) {
        this.name = name;
    }

    public Server getServerFromLoadBalancer(final Object loadBalancerKey) {
        return null;
    }

    @Override
    public String getClientName() {
        return null;
    }
}
