package com.xqbase.baiji.rpc.server.tomcat;

/**
 * Simple interface that represents a fully configured embedded servlet container (for
 * example Tomcat or Jetty). Allows the container to be {@link #start() started} and
 * {@link #stop() stopped}.
 * <p>
 * Instances of this class are usually obtained via a
 * {@link EmbeddedServletContainerFactory}.
 *
 * @author Tony He
 */
public interface EmbeddedServletContainer {

    /**
     * Starts the embedded servlet container. Calling this method on an already started
     * container has no effect.
     * @throws EmbeddedServletContainerException if the container cannot be started
     */
    void start() throws EmbeddedServletContainerException;

    /**
     * Stops the embedded servlet container. Calling this method on an already stopped
     * container has no effect.
     * @throws EmbeddedServletContainerException if the container cannot be stopped
     */
    void stop() throws EmbeddedServletContainerException;

    /**
     * @return the port this server is listening on (or -1 if none)
     */
    int getPort();
}
