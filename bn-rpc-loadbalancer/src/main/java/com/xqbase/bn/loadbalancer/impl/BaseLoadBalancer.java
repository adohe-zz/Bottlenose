package com.xqbase.bn.loadbalancer.impl;

import com.xqbase.bn.loadbalancer.Ping;
import com.xqbase.bn.loadbalancer.Rule;
import com.xqbase.bn.loadbalancer.Server;
import com.xqbase.bn.loadbalancer.stats.LoadBalancerStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
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

    private boolean canSkipPing() {
        return true;
    }

    private void setupPingTask() {
        if (canSkipPing()) {
            return;
        }
    }

    /**
     * Add a server to the 'allServer' list; does not verify uniqueness, so you
     * could give a server a greater share by adding it more than once.
     */
    public void addServer(Server newServer) {
        if (newServer != null) {
            try {
                ArrayList<Server> newList = new ArrayList<Server>();

                newList.addAll(allServerList);
                newList.add(newServer);
                setServersList(newList);
            } catch (Exception e) {
                LOGGER.error("Exception while adding a newServer", e);
            }
        }
    }

    @Override
    public void addServers(List<Server> newServers) {
        if (newServers != null && newServers.size() > 0) {
            try {
                ArrayList<Server> newList = new ArrayList<Server>();

                newList.addAll(allServerList);
                newList.addAll(newServers);
                setServersList(newList);
            } catch (Exception e) {
                LOGGER.error("Exception while adding servers", e);
            }
        }
    }

    /**
     * Set the list of servers used as the server pool. This overrides existing
     * server list.
     */
    public void setServersList(List list) {
        Lock writeLock = allServerLock.writeLock();
        ArrayList<Server> allServers = new ArrayList<>();

        for (Object s : list) {
            if (null == s) {
                continue;
            }
            if (s instanceof String) {
                allServers.add(new Server((String) s));
            }
            if (s instanceof Server) {
                allServers.add((Server) s);
            } else {
                throw new IllegalArgumentException("Only String or Server instance expected here, instead found: "
                    + s.getClass());
            }
        }

        writeLock.lock();
        try {
            allServerList = allServers;

            if (canSkipPing()) {
                for (Server s : allServerList) {
                    s.setIsAlive(true);
                }
                upServerList = allServerList;
            }
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public Server choose(Object key) {
        if (null == rule) {
            return null;
        } else {
            try {
                return rule.choose(key);
            } catch (Throwable t) {
                return null;
            }
        }
    }

    @Override
    public void markServerDown(Server server) {
        if (null == server)
            return;

        if (!server.isAlive())
            return;

        server.setIsAlive(false);
    }

    @Override
    public List<Server> getServersList(boolean availableOnly) {
        return (availableOnly ? Collections.unmodifiableList(upServerList) :
                Collections.unmodifiableList(allServerList));
    }

    @Override
    public List<Server> getServerList(ServerGroup serverGroup) {
        switch (serverGroup) {
            case ALL:
                return allServerList;
            case STATUS_UP:
                return upServerList;
            case STATUS_NOT_UP:
                ArrayList<Server> allServers = new ArrayList<>(allServerList);
                ArrayList<Server> upServers = new ArrayList<>(upServerList);
                allServers.removeAll(upServers);
                return allServers;
        }
        return new ArrayList<>();
    }

    @Override
    public LoadBalancerStats getStats() {
        return this.lbStats;
    }
}
