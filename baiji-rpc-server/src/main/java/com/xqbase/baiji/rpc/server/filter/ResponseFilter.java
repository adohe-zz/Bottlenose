package com.xqbase.baiji.rpc.server.filter;

import com.xqbase.baiji.rpc.server.context.ServiceContext;
import com.xqbase.baiji.rpc.server.r2.HttpRequestWrapper;
import com.xqbase.baiji.rpc.server.r2.HttpResponseWrapper;

/**
 * Defines a request filter to be executed after executing the operation and before sending the response.
 *
 * @author Tony He
 */
public interface ResponseFilter {

    void apply(ServiceContext context, HttpRequestWrapper request, HttpResponseWrapper response);
}
