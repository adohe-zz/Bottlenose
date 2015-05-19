package com.xqbase.baiji.transport.bridge.client;

import com.xqbase.baiji.common.callback.Callback;
import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.RequestContext;
import com.xqbase.baiji.m2.Response;
import com.xqbase.baiji.transport.AbstractClient;
import com.xqbase.baiji.transport.Client;

import java.util.concurrent.Future;

/**
 * TransportClientAdapter wrap the {@link TransportClient}
 * and implement the request method.
 *
 * @author Tony He
 */
public class TransportClientAdapter extends AbstractClient {

    private TransportClient client;

    public TransportClientAdapter(TransportClient client) {
        this.client = client;
    }

    @Override
    public void sendRequest(Request request, RequestContext requestContext, Callback<Response> callback) {
        client.request(request, requestContext, new TransportCallbackAdapter<Response>(callback));
    }
}
