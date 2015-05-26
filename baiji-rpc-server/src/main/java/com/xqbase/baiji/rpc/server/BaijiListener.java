package com.xqbase.baiji.rpc.server;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This listener will configure {@link javax.servlet.ServletContext} before
 * any servlet creation.
 *
 * @author Tony He
 */
public class BaijiListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
