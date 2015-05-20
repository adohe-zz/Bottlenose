package com.xqbase.baiji.transport.tcp.client;

import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.RequestContext;
import com.xqbase.baiji.m2.Response;
import com.xqbase.baiji.transport.bridge.client.TransportClient;
import com.xqbase.baiji.transport.bridge.common.TransportCallback;

/**
 * TcpClient based on Netty.
 *
 * @author Tony He
 */
public class TcpNettyClient implements TransportClient {

    @Override
    public void request(Request request, RequestContext requestContext, TransportCallback<Response> callback) {

    }
}
