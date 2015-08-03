package com.xqbase.baiji.rpc.server.filter;

import com.xqbase.baiji.rpc.server.context.ServiceContext;
import com.xqbase.baiji.rpc.server.r2.HttpRequestWrapper;
import com.xqbase.baiji.rpc.server.r2.HttpResponseWrapper;

/**
 * Defines a request filter to be executed before deserialize request object.
 *
 * @author Tony He
 */
public interface PreRequestFilter {

    void apply(ServiceContext context, HttpRequestWrapper request, HttpResponseWrapper response);
}
