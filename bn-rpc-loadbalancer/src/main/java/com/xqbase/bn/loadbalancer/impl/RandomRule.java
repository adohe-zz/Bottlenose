package com.xqbase.bn.loadbalancer.impl;

import com.xqbase.bn.common.logging.Logger;
import com.xqbase.bn.common.logging.LoggerFactory;
import com.xqbase.bn.loadbalancer.LoadBalancer;
import com.xqbase.bn.loadbalancer.Server;

import java.util.List;
import java.util.Random;

/**
 * A loadbalacing strategy that randomly distributes traffic amongst existing
 * servers.
 *
 * @author Tony He
 */
public class RandomRule extends AbstractLoadBalancerRule {

    private static final Logger logger = LoggerFactory.getLogger(RandomRule.class);

    private Random random;

    public RandomRule() {
        this.random = new Random();
    }

    private Server choose(LoadBalancer lb, Object key) {
        if (null == lb) {
            logger.warn("no load balancer");
            return null;
        }
        Server server = null;

        while (null == server) {
            if (Thread.interrupted()) {
                return null;
            }

            List<Server> allList = lb.getServersList(false);
            List<Server> upList = lb.getServersList(true);
            int allCount = allList.size();
            int upCount = upList.size();

            if (allCount == 0 || upCount == 0) {
                logger.warn("No up instance available from load balancer: " + lb);
                return null;
            }

            int index = random.nextInt(upCount);
            server = upList.get(index);

            if (null == server) {
                /*
                 * The only time this should happen is if the server list were
                 * somehow trimmed. This is a transient condition. Retry after
                 * yielding.
                 */
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            // Shouldn't actually happen.. but must be transient or a bug.
            server = null;
            Thread.yield();
        }
        return server;
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }
}
