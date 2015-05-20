package com.xqbase.baiji.transport.http.client;

import com.xqbase.baiji.common.callback.Callback;
import com.xqbase.baiji.transport.apool.LifeCycle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.group.ChannelGroup;

import java.net.SocketAddress;

/**
 * LifeCycle for Async Channel Pool.
 *
 * @author Tony He
 */
public class ChannelPoolLifeCycle<Channel> implements LifeCycle<Channel> {

    private final Bootstrap bootstrap;
    private final SocketAddress address;
    private final ChannelGroup channelGroup;

    public ChannelPoolLifeCycle(Bootstrap bootstrap, SocketAddress address, ChannelGroup channelGroup) {
        this.bootstrap = bootstrap;
        this.address = address;
        this.channelGroup = channelGroup;
    }

    @Override
    public void create(Callback<Channel> callback) {

    }

    @Override
    public boolean validateGet(Channel obj) {
        return false;
    }

    @Override
    public boolean validatePut(Channel obj) {
        return false;
    }

    @Override
    public void destroy(Channel obj, boolean error, Callback<Channel> callback) {

    }
}
