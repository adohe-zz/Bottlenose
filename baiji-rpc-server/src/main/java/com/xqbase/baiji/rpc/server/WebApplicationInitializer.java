package com.xqbase.baiji.rpc.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Web Application Initializer.
 *
 * @author Tony He
 */
public interface WebApplicationInitializer {

    void onStartup(ServletContext servletContext) throws ServletException;
}
