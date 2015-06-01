package com.xqbase.baiji.transport.http.client;

import com.xqbase.baiji.m2.http.HttpResponse;
import com.xqbase.baiji.transport.bridge.client.InboundHandlerWithAttachment;
import com.xqbase.baiji.transport.bridge.common.TransportCallback;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

/**
 * Netty pipeline handler which takes a complete received message and invoke
 * the specified actions.
 *
 * @author Tony He
 */
public class HttpResponseHandler extends InboundHandlerWithAttachment<TransportCallback<HttpResponse>> {

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception {
        super.close(ctx, promise);
    }
}
