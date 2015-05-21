package com.xqbase.baiji.transport.http.client;

import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.RequestContext;
import com.xqbase.baiji.m2.Response;
import com.xqbase.baiji.transport.bridge.client.TransportClient;
import com.xqbase.baiji.transport.bridge.common.TransportCallback;

/**
 * HttpClient based on Netty.
 *
 * @author Tony He
 */
public class HttpNettyClient implements TransportClient {

    private final ChannelPoolManager poolManager;

    public HttpNettyClient(ChannelPoolManager poolManager) {
        this.poolManager = poolManager;
    }

    @Override
    public void request(Request request, RequestContext requestContext, TransportCallback<Response> callback) {
    }
}
