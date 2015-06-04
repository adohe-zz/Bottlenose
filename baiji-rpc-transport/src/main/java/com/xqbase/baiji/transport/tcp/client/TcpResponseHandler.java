package com.xqbase.baiji.transport.tcp.client;

import com.xqbase.baiji.m2.tcp.TcpResponse;
import com.xqbase.baiji.transport.bridge.client.InboundHandlerWithAttachment;
import com.xqbase.baiji.transport.bridge.common.TransportCallback;
import io.netty.channel.ChannelHandlerContext;

/**
 * The Tcp Response Handler.
 *
 * @author Tony He
 */
public class TcpResponseHandler extends InboundHandlerWithAttachment<TransportCallback<TcpResponse>> {
    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
