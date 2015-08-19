package com.xqbase.bn.rpc.server.dispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Baiji Service host will handle all incoming requests, and dispatch
 * to corresponding service instance. This is something like DispatcherServlet.
 *
 * @author Tony He
 */
public class BaijiServiceDispatcher implements ServiceDispatcher {

    @Override
    public void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception {

    }
}
