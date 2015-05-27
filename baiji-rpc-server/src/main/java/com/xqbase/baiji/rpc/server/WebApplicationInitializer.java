package com.xqbase.baiji.rpc.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

/**
 * Interface in order to configure ServletContext with code-base approach.
 *
 * @author Tony He
 */
public interface WebApplicationInitializer {

    /**
     * Configure the given {@link javax.servlet.ServletContext} with any servlets, filters,
     * listeners context-params and attributes necessary for initializing the application.
     *
     * @throws javax.servlet.ServletException
     */
    void onStartup(ServletContext servletContext) throws ServletException;
}
