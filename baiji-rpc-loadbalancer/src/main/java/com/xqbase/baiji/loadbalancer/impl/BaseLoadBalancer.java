package com.xqbase.baiji.loadbalancer.impl;

import com.xqbase.baiji.loadbalancer.Ping;
import com.xqbase.baiji.loadbalancer.Rule;
import com.xqbase.baiji.loadbalancer.Server;
import com.xqbase.baiji.loadbalancer.stats.LoadBalancerStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A basic implementation of the load balancer where an arbitrary list of
 * servers can be set as the server pool. A ping can be set to determine the
 * liveness of a server. Internally, this class maintains an "all" server list
 * and an "up" server list and use them depending on what the caller asks for.
 *
 * @author Tony He
 *
 */
public class BaseLoadBalancer extends AbstractLoadBalancer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BaseLoadBalancer.class);

    private static final Rule DEFAULT_RULE = new RoundRobinRule();
    private static final String DEFAULT_NAME = "default";

    protected volatile List<Server> allServerList = Collections
            .synchronizedList(new ArrayList<Server>());
    protected volatile List<Server> upServerList = Collections
            .synchronizedList(new ArrayList<Server>());

    protected ReadWriteLock allServerLock = new ReentrantReadWriteLock();
    protected ReadWriteLock upServerLock = new ReentrantReadWriteLock();

    protected Rule rule = DEFAULT_RULE;
    protected String name = DEFAULT_NAME;
    protected Ping ping = null;

    protected LoadBalancerStats lbStats;

    /**
     * Default constructor which sets name as "default", sets null ping, and
     * {@link RoundRobinRule} as the rule.
     * <p>
     */
    public BaseLoadBalancer() {
        this.name = DEFAULT_NAME;
        setRule(DEFAULT_RULE);
        setupPingTask();
        this.lbStats = new LoadBalancerStats(DEFAULT_NAME);
    }

    public BaseLoadBalancer(String lbName, Rule rule, LoadBalancerStats lbStats) {
        this(lbName, rule, lbStats, null);
    }

    public BaseLoadBalancer(String lbName, Rule rule, LoadBalancerStats lbStats, Ping ping) {
        this.name = lbName;
        this.ping = ping;
        setRule(rule);
        setupPingTask();
        this.lbStats = lbStats;
    }

    private void setRule(Rule rule) {
        if (rule != null) {
            this.rule = rule;
        } else {
            this.rule = DEFAULT_RULE;
        }
        if (this.rule.getLoadBalancer() != this) {
            this.rule.setLoadBalancer(this);
        }
    }

    private void setupPingTask() {

    }

    @Override
    public void addServers(List<Server> newServers) {

    }

    @Override
    public Server choose(Object key) {
        return null;
    }

    @Override
    public void markServerDown(Server server) {

    }

    @Override
    public List<Server> getServersList(boolean availableOnly) {
        return null;
    }
}
