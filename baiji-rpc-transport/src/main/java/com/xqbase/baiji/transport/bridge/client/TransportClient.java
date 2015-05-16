package com.xqbase.baiji.transport.bridge.client;

import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.RequestContext;
import com.xqbase.baiji.m2.Response;
import com.xqbase.baiji.transport.bridge.common.TransportCallback;

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
}
