package com.xqbase.bn.transport.bridge.client;

import com.xqbase.bn.transport.apool.AsyncPool;
import io.netty.channel.Channel;

import java.net.SocketAddress;

/**
 * Factory for creating channel pool.
 *
 * @author Tony He
 */
public interface ChannelPoolFactory {

    /**
     * Create a channel pool for this address.
     */
    AsyncPool<Channel> getPool(SocketAddress address);
}
