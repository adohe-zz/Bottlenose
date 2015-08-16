package com.xqbase.bn.transport.tcp.client;

import com.xqbase.bn.common.callback.Callback;
import com.xqbase.bn.m2.Request;
import com.xqbase.bn.m2.RequestContext;
import com.xqbase.bn.m2.Response;
import com.xqbase.bn.transport.apool.util.None;
import com.xqbase.bn.transport.bridge.client.TransportClient;
import com.xqbase.bn.transport.bridge.common.TransportCallback;

/**
 * TcpClient based on Netty.
 *
 * @author Tony He
 */
public class TcpNettyClient implements TransportClient {

    @Override
    public void request(Request request, RequestContext requestContext, TransportCallback<Response> callback) {

    }

    @Override
    public void shutdown(Callback<None> callback) {

    }
}
