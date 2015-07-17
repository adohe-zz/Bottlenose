package com.xqbase.baiji.loadbalancer.stats;

import com.google.common.cache.*;
import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.xqbase.baiji.loadbalancer.Server;

import java.util.concurrent.TimeUnit;

/**
 * Class that acts as a repository of operational characteristics and statistics
 * of every Node/Server in the LoadBalancer.
 *
 * This information can be used to just observe and understand the runtime
 * behavior of the loadbalancer or more importantly for the basis that
 * determines the loadbalacing strategy
 *
 * @author Tony He
 */
public class LoadBalancerStats {

    private static final String PREFIX = "LBStats_";

    private String name;

    private static final DynamicIntProperty SERVERSTATS_EXPIRE_MINUTES =
            DynamicPropertyFactory.getInstance().getIntProperty("baiji.loadbalancer.serverStats.expire.minutes", 30);

    private final LoadingCache<Server, ServerStats> serverStatsCache =
            CacheBuilder.newBuilder()
                    .expireAfterAccess(SERVERSTATS_EXPIRE_MINUTES.get(), TimeUnit.MINUTES)
                    .removalListener(new RemovalListener<Server, ServerStats>() {
                        @Override
                        public void onRemoval(RemovalNotification<Server, ServerStats> notification) {
                            notification.getValue().close();
                        }
                    })
                    .build(
                            new CacheLoader<Server, ServerStats>() {
                                public ServerStats load(Server server) {
                                    return createServerStats(server);
                                }
                            });

    private ServerStats createServerStats(Server server) {
        ServerStats ss = new ServerStats();
        //configure custom settings
        ss.setBufferSize(1000);
        ss.setPublishInterval(1000);
        ss.initialize(server);
        return ss;
    }

    public LoadBalancerStats(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
