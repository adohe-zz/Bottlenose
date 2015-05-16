package com.xqbase.baiji.transport;

import com.xqbase.baiji.common.callback.Callback;
import com.xqbase.baiji.m2.Request;
import com.xqbase.baiji.m2.RequestContext;
import com.xqbase.baiji.m2.Response;

import java.util.concurrent.Future;

/**
 * The abstract transport layer.
 *
 * @author Tony He
 */
public interface Client {

    /**
     * Asynchronously issue the given request and returns a {@link Future}
     * that can be used to wait for a response.
     *
     * @param request request to issue.
     * @return a {@link Future} to wait for response.
     */
    Future<Response> sendRequest(Request request);

    /**
     * Asynchronously issue the given request and returns a {@link Future}
     * that can be used to wait for a response.
     *
     * @param request request to issue.
     * @param requestContext context of this request.
     * @return a {@link Future} to wait for response.
     */
    Future<Response> sendRequest(Request request, RequestContext requestContext);

    /**
     * Asynchronously issue the given request and invokes the callback
     * when response received.
     *
     * @param request request to issue.
     * @param callback callback to be invoked.
     */
    void sendRequest(Request request, Callback<Response> callback);

    /**
     * Asynchronously issue the given request and invokes the callback
     * when response received.
     *
     * @param request request to issue.
     * @param requestContext context of this request.
     * @param callback callback to be invoked.
     */
    void sendRequest(Request request, RequestContext requestContext, Callback<Response> callback);
}
