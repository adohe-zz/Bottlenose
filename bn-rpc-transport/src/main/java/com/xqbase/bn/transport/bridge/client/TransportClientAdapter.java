package com.xqbase.bn.transport.bridge.client;

import com.xqbase.bn.common.callback.Callback;
import com.xqbase.bn.m2.Request;
import com.xqbase.bn.m2.RequestContext;
import com.xqbase.bn.m2.Response;
import com.xqbase.bn.transport.AbstractClient;

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
