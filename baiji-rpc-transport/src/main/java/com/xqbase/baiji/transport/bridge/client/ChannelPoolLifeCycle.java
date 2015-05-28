package com.xqbase.baiji.transport.bridge.client;

import com.xqbase.baiji.common.callback.Callback;
import com.xqbase.baiji.transport.apool.LifeCycle;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.Channel;
import java.net.SocketAddress;

/**
 * LifeCycle for Async Channel Pool.
 *
 * @author Tony He
 */
public class ChannelPoolLifeCycle implements LifeCycle<Channel> {

    private final Bootstrap bootstrap;
    private final SocketAddress address;
    private final ChannelGroup channelGroup;

    public ChannelPoolLifeCycle(Bootstrap bootstrap, SocketAddress address, ChannelGroup channelGroup) {
        this.bootstrap = bootstrap;
        this.address = address;
        this.channelGroup = channelGroup;
    }

    @Override
    public void create(final Callback<Channel> callback) {
        this.bootstrap.connect(address).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                Channel c = future.channel();
                // add the ChannelPoolHandler
                c.pipeline().addLast(new ChannelPoolHandler());
                channelGroup.add(c);
                callback.onSuccess(c);
            }
        });
    }

    @Override
    public boolean validateGet(Channel c) {
        return c.isActive();
    }

    @Override
    public boolean validatePut(Channel c) {
        return c.isActive();
    }

    @Override
    public void destroy(Channel c, boolean error, final Callback<Channel> callback) {
        if (c.isActive()) {
            c.close().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        callback.onSuccess(future.channel());
                    }
                }
            });
        } else {
            callback.onSuccess(c);
        }
    }
}
