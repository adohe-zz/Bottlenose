package com.xqbase.baiji.rpc.server.filter;

import com.xqbase.baiji.rpc.server.context.ServiceContext;
import com.xqbase.baiji.rpc.server.r2.HttpRequestWrapper;
import com.xqbase.baiji.rpc.server.r2.HttpResponseWrapper;

/**
 * Defines a request filter to be executed after deserialize request object
 * and before executing the operation.
 *
 * @author Tony He
 */
public interface RequestFilter {

    void apply(ServiceContext context, HttpRequestWrapper request, HttpResponseWrapper response);
}
