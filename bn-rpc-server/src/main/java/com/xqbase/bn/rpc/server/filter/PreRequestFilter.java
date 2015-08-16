package com.xqbase.bn.rpc.server.filter;

import com.xqbase.bn.rpc.server.context.ServiceContext;
import com.xqbase.bn.rpc.server.r2.HttpRequestWrapper;
import com.xqbase.bn.rpc.server.r2.HttpResponseWrapper;

/**
 * Defines a request filter to be executed before deserialize request object.
 *
 * @author Tony He
 */
public interface PreRequestFilter {

    void apply(ServiceContext context, HttpRequestWrapper request, HttpResponseWrapper response);
}
