package com.xqbase.bn.transport.bridge.client;

import com.xqbase.bn.common.callback.Callback;
import com.xqbase.bn.m2.Request;
import com.xqbase.bn.m2.RequestContext;
import com.xqbase.bn.m2.Response;
import com.xqbase.bn.transport.apool.util.None;
import com.xqbase.bn.transport.bridge.common.TransportCallback;

/**
 * Interface for real different transport mechanism.
 */
public interface TransportClient {

    /**
     * Asynchronously issue the given request. And the given callback
     * is invoked when the response received.
     *
     * @param request request to issue.
     * @param requestContext context for this request.
     * @param callback response callback.
     */
    void request(Request request,
                 RequestContext requestContext,
                 TransportCallback<Response> callback);

    /**
     * Starts asynchronous shutdown of the client.
     *
     * @param callback a callback that will be invoked once the shutdown is complete.
     */
    void shutdown(Callback<None> callback);
}
