package com.xqbase.baiji.loadbalancer.context;

import com.xqbase.baiji.client.config.IClientConfig;
import com.xqbase.baiji.common.logging.Logger;
import com.xqbase.baiji.common.logging.LoggerFactory;
import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.Server;
import com.xqbase.baiji.loadbalancer.stats.ServerStats;

/**
 * A class contains APIs intended to be used be load balancing client which is subclass of this class.
 *
 * @author Tony He
 */
public class LoadBalancerContext implements IClientConfig {

    private static final Logger logger = LoggerFactory.getLogger(LoadBalancerContext.class);

    private static final String DEFAULT_NAME = "default";

    private String name = DEFAULT_NAME;
    private LoadBalancer lb;

    public LoadBalancerContext(LoadBalancer lb) {
        this.lb = lb;
    }

    public Server getServerFromLoadBalancer(final Object loadBalancerKey) {
        return null;
    }

    public LoadBalancer getLoadBalancer() {
        return lb;
    }

    public void setLoadBalancer(LoadBalancer lb) {
        this.lb = lb;
    }

    private void recordStats(ServerStats stats, long responseTime) {
        stats.decrementActiveRequestsCount();
        stats.noteResponseTime(responseTime);
        stats.incrementNumRequests();
    }

    public void noteRequestCompletion(ServerStats stats, Object response, Throwable t, long responseTime) {

    }

    public void noteError(ServerStats stats, Throwable e, long responseTime) {

    }

    public void noteResponse(ServerStats stats, Object response, long responseTime) {

    }

    public void noteOpenConnection(ServerStats stats) {
        if (null == stats) {
            return;
        }
        try {
            stats.incrementActiveRequestsCount();
        } catch (Throwable e) {
            logger.info("Unable to note Server Stats:", e);
        }
    }

    @Override
    public String getClientName() {
        return null;
    }
}
