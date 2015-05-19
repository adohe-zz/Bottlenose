package com.xqbase.baiji.transport.bridge.client;

import com.xqbase.baiji.common.callback.Callback;
import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.RequestContext;
import com.xqbase.baiji.m2.Response;
import com.xqbase.baiji.transport.Client;

import java.util.concurrent.Future;

/**
 * Created by nankonami on 15-5-16.
 */
public class TransportClientAdapter implements Client {

    private TransportClient client;

    @Override
    public Future<Response> sendRequest(Request request) {
        return null;
    }

    @Override
    public Future<Response> sendRequest(Request request, RequestContext requestContext) {
        return null;
    }

    @Override
    public void sendRequest(Request request, Callback<Response> callback) {

    }

    @Override
    public void sendRequest(Request request, RequestContext requestContext, Callback<Response> callback) {

    }
}
