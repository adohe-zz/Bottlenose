package com.xqbase.bn.rpc.server.context;

/**
 * Servlet Context maintains configuration for per Web Application per
 * Java Virtual Machine. This is corresponding to Tomcat ServletContext.
 *
 * @author Tony He
 */
public class BaijiServletContext {

    public static final BaijiServletContext INSTANCE = new BaijiServletContext();

    private BaijiServletContext() {
    }
}
