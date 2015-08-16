package com.xqbase.bn.rpc.server.jetty;

import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * Jetty {@link org.eclipse.jetty.webapp.WebAppContext} used by {@link JettyEmbeddedServletContainer} to support
 * deferred initialization.
 *
 * @author Tony He
 */
public class JettyEmbeddedWebAppContext extends WebAppContext {

    @Override
    protected ServletHandler newServletHandler() {
        return new JettyEmbeddedServletHandler();
    }

    public void deferredInitialize() throws Exception {
        ((JettyEmbeddedServletHandler) getServletHandler()).deferredInitialize();
    }

    private static class JettyEmbeddedServletHandler extends ServletHandler {

        @Override
        public void initialize() throws Exception {
        }

        public void deferredInitialize() throws Exception {
            super.initialize();
        }

    }
}
