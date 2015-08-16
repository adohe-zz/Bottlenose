package com.xqbase.bn.transport.bridge.client;

import com.xqbase.bn.transport.apool.AsyncPool;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Listens for inbound events affecting the state of the channel as
 * it relates to the pool. This handler does not call super because
 * it expects to be the last handler in the pipeline, to ensure that
 * every other handler has had a chance to process the event and finish
 * with the channel.
 *
 * Basically, the handler's job is to return the channel to the pool,
 * or ask the pool to dispose of the channel, after the response is
 * received or after an error occurs.
 *
 * @author Tony He
 */
public class ChannelPoolHandler extends InboundHandlerWithAttachment<AsyncPool<Channel>> {

    public ChannelPoolHandler() {
    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
        AsyncPool<Channel> pool = removeAttachment(ctx);
        if (pool != null) {
            pool.put(ctx.pipeline().channel());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        AsyncPool<Channel> pool = removeAttachment(ctx);
        if (pool != null) {
            pool.dispose(ctx.pipeline().channel());
        }
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        AsyncPool<Channel> pool = removeAttachment(ctx);
        if (pool != null) {
            pool.dispose(ctx.pipeline().channel());
        }
    }
}
