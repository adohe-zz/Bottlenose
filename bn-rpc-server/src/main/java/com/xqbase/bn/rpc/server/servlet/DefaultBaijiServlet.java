package com.xqbase.bn.rpc.server.servlet;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * The default Baiji servlet.
 *
 * @author Tony He
 */
public class DefaultBaijiServlet extends BaijiServlet {

    @Override
    public void service(ServletRequest req, ServletResponse res)
            throws ServletException, IOException {
        processRequest(req, res);
    }
}
