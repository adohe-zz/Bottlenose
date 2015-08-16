package com.xqbase.bn.rpc.server.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * The default Baiji servlet.
 *
 * @author Tony He
 */
public class DefaultBaijiServlet extends BaijiServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.service(req, resp);
    }
}
