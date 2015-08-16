package com.xqbase.bn.transport.tcp.client;

import com.xqbase.bn.m2.tcp.TcpResponse;
import com.xqbase.bn.transport.bridge.client.InboundHandlerWithAttachment;
import com.xqbase.bn.transport.bridge.common.TransportCallback;
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
