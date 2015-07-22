package com.xqbase.baiji.loadbalancer.context;

import com.xqbase.baiji.client.config.IClientConfig;
import com.xqbase.baiji.common.logging.Logger;
import com.xqbase.baiji.common.logging.LoggerFactory;
import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.Server;
import com.xqbase.baiji.loadbalancer.impl.AbstractLoadBalancer;
import com.xqbase.baiji.loadbalancer.stats.LoadBalancerStats;
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
        LoadBalancer lb = getLoadBalancer();
        if (lb != null) {
            Server svc = lb.choose(loadBalancerKey);
            if (null == svc) {

            }
            return svc;
        }
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

    /**
     * This is called after a response is received or an exception is thrown from the client
     * to update related stats.
     */
    public void noteRequestCompletion(ServerStats stats, Object response, Throwable t, long responseTime) {
        try {
            recordStats(stats, responseTime);
            if (response != null) {
                stats.clearSuccessiveConnectionFailureCount();
            } else if (t != null) {

            }
        } catch (Throwable ex) {
            logger.error("Unexpected exception", ex);
        }
    }

    /**
     * This is called after an error is thrown from the client
     * to update related stats.
     */
    public void noteError(ServerStats stats, Throwable e, long responseTime) {
        try {
            recordStats(stats, responseTime);
            if (e != null) {
                stats.incrementSuccessiveConnectionFailureCount();
            }
        } catch (Throwable ex) {
            logger.error("Unexpected exception", ex);
        }
    }

    /**
     * This is called after a response is received from the client
     * to update related stats.
     */
    public void noteResponse(ServerStats stats, Object response, long responseTime) {
        try {
            recordStats(stats, responseTime);
            if (response != null) {
                stats.clearSuccessiveConnectionFailureCount();
            }
        } catch (Throwable ex) {
            logger.error("Unexpected exception", ex);
        }
    }

    /**
     * This is usually called just before client execute a request.
     */
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

    public final ServerStats getServerStats(final Server server) {
        ServerStats serverStats = null;
        LoadBalancer lb = this.getLoadBalancer();
        if (lb instanceof AbstractLoadBalancer){
            LoadBalancerStats lbStats = ((AbstractLoadBalancer) lb).getStats();
            serverStats = lbStats.getSingleServerStat(server);
        }
        return serverStats;
    }

    @Override
    public String getClientName() {
        return name;
    }
}
