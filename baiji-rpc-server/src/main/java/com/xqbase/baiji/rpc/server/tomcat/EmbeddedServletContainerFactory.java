package com.xqbase.baiji.rpc.server.tomcat;

/**
 * Factory interface that can be used to create {@link EmbeddedServletContainer}s.
 *
 * @author Tony He
 */
public interface EmbeddedServletContainerFactory {

    /**
     * Gets a new fully configured but paused {@link EmbeddedServletContainer} instance.
     * Clients should not be able to connect to the returned server until
     * {@link EmbeddedServletContainer#start()} is called (which happens when the
     * {@link org.apache.catalina.core.ApplicationContext} has been fully refreshed).
     * the container starts
     * @return a fully configured and started {@link EmbeddedServletContainer}
     * @see EmbeddedServletContainer#stop()
     */
    EmbeddedServletContainer getEmbeddedServletContainer();
}
