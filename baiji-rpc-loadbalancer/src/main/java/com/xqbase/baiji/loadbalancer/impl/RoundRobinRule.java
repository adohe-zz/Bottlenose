package com.xqbase.baiji.loadbalancer.impl;

import com.xqbase.baiji.common.logging.Logger;
import com.xqbase.baiji.common.logging.LoggerFactory;
import com.xqbase.baiji.loadbalancer.LoadBalancer;
import com.xqbase.baiji.loadbalancer.Server;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * The most well known and basic loadbalacing strategy, i.e. Round Robin Rule.
 *
 * @author Tony He
 */
public class RoundRobinRule extends AbstractLoadBalancerRule {

    private static final Logger logger = LoggerFactory.getLogger(RoundRobinRule.class);

    private AtomicLong nextIndexAI;

    public RoundRobinRule() {
        this.nextIndexAI = new AtomicLong(0);
    }

    private Server choose(LoadBalancer lb, Object key) {
        if (null == lb) {
            logger.warn("no load balancer");
            return null;
        }

        Server server = null;
        int index;

        int count = 0;
        while (null == server && count ++ < 10) {
            List<Server> allList = lb.getServersList(false);
            List<Server> upList = lb.getServersList(true);
            int upCount = upList.size();
            int allCount = allList.size();

            if (upCount == 0 || allCount == 0) {
                logger.warn("No up instance available from load balancer: " + lb);
                return null;
            }

            index = (int) nextIndexAI.incrementAndGet() % allCount;
            server = allList.get(index);

            if (null == server) {
                // Transient
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return server;
            }

            server = null;
        }
        return server;
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }
}
