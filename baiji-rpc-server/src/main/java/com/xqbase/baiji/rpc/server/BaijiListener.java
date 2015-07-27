package com.xqbase.baiji.rpc.server;

import com.xqbase.baiji.common.logging.Logger;
import com.xqbase.baiji.common.logging.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * This listener will configure {@link javax.servlet.ServletContext} before
 * any servlet creation.
 *
 * @author Tony He
 */
public class BaijiListener implements ServletContextListener {

    private static final String REGISTRY_SERVICE_URL = "registry-service-url";
    private static final String SERVICE_PORT_PARAM = "service-port";
    private static final String SUB_ENV_PARAM = "sub-env";
    private static final String SERVICE_CLASS_PARAM = "service-class";
    private static final String REUSE_SERVICE_PARAM = "reuse-service";
    private static final String DEBUG_MODE_PARAM = "debug";

    private static final Logger logger = LoggerFactory.getLogger(BaijiListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
