package com.xqbase.bn.rpc.server.dispatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * DispatcherServlet Interface.
 *
 * @author Tony He
 */
public interface ServiceDispatcher {

    void doDispatch(HttpServletRequest req, HttpServletResponse resp) throws Exception;
}
