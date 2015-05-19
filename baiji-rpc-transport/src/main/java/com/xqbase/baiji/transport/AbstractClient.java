package com.xqbase.baiji.transport;

import com.xqbase.baiji.common.callback.Callback;
import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.RequestContext;
import com.xqbase.baiji.m2.Response;

import java.util.concurrent.Future;

/**
 * Add default implementation for client.
 *
 * @author Tony He
 */
public abstract class AbstractClient implements Client {

    private static final RequestContext EMPTY_CONTEXT = new RequestContext();

    @Override
    public Future<Response> sendRequest(Request request) {
        return sendRequest(request, EMPTY_CONTEXT);
    }

    @Override
    public Future<Response> sendRequest(Request request, RequestContext requestContext) {
        return null;
    }

    @Override
    public void sendRequest(Request request, Callback<Response> callback) {
        sendRequest(request, EMPTY_CONTEXT, callback);
    }
}
