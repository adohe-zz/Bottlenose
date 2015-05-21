package com.xqbase.baiji.transport.http.client;

import com.xqbase.baiji.transport.apool.AsyncPool;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A collection of Channel Pools.
 *
 * @author Tony He
 */
public class ChannelPoolManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChannelPoolManager.class);

    // The mutex lock
    private final Object mutex = new Object();
    private final ConcurrentHashMap<SocketAddress, AsyncPool<Channel>>
                pools = new ConcurrentHashMap<>(256, 0.75f, 1);

    private enum State { RUNNING, SHUTTING_DOWN, SHUT_DOWN }
    private State state = State.RUNNING;

    private final ChannelPoolFactory channelPoolFactory;
    private final String name;

    public ChannelPoolManager(ChannelPoolFactory channelPoolFactory, String name) {
        this.channelPoolFactory = channelPoolFactory;
        this.name = name;
    }

    public AsyncPool<Channel> getPoolForAddress(SocketAddress address) {
        // unsynchronized get is safe here
        AsyncPool<Channel> pool = pools.get(address);
        if (pool != null) {
            return pool;
        }

        synchronized (mutex) {
            if (state != State.RUNNING) {
                throw new IllegalStateException("Pool manager is down");
            }
            // double check
            pool = pools.get(address);
            if (null == pool) {
                pool = channelPoolFactory.getPool(address);
                pool.start();
                pools.put(address, pool);
            }
        }
        return pool;
    }
}
