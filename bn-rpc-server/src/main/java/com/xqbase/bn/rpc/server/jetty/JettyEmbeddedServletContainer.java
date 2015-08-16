package com.xqbase.bn.rpc.server.jetty;

import com.xqbase.bn.common.logging.Logger;
import com.xqbase.bn.common.logging.LoggerFactory;
import com.xqbase.bn.common.util.ReflectionUtils;
import com.xqbase.bn.common.util.StringUtils;
import com.xqbase.bn.rpc.server.tomcat.EmbeddedServletContainer;
import com.xqbase.bn.rpc.server.tomcat.EmbeddedServletContainerException;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.HandlerWrapper;

import java.util.List;

/**
 * {@link EmbeddedServletContainer} that can be used to control an embedded Jetty server.
 *
 * @author Tony He
 */
public class JettyEmbeddedServletContainer implements EmbeddedServletContainer {

    private static final Logger logger = LoggerFactory
            .getLogger(JettyEmbeddedServletContainer.class);

    private final Server server;
    private boolean autoStart;
    private Connector[] connectors;

    public JettyEmbeddedServletContainer(final Server server) {
        this(server, true);
    }

    public JettyEmbeddedServletContainer(final Server server, boolean autoStart) {
        this.server = server;
        this.autoStart = autoStart;
        initialize();
    }

    private synchronized void initialize() {
        try {
            // Cache and clear the connectors to prevent requests being handled before
            // the application context is ready
            this.connectors = this.server.getConnectors();
            this.server.setConnectors(null);

            // Start the server so that the ServletContext is available
            this.server.start();
        } catch (Exception e) {
            stopSilently();
            throw new EmbeddedServletContainerException("Unable to start Embedded Jetty", e);
        }
    }

    private void stopSilently() {
        try {
            this.server.stop();
        }
        catch (Exception ex) {/**Ignored*/}
    }

    @Override
    public void start() throws EmbeddedServletContainerException {
        this.server.setConnectors(this.connectors);
        if (!this.autoStart)
            return;

        try {
            this.server.start();
            for (Handler handler : this.server.getHandlers()) {
                handleDeferredInitialize(handler);
            }
            Connector[] connectors = this.server.getConnectors();
            for (Connector connector : connectors) {
                connector.start();
            }
            JettyEmbeddedServletContainer.logger.info("Jetty started on port(s) "
                    + getActualPortsDescription());
        } catch (Exception e) {
            throw new EmbeddedServletContainerException("Unable to start Embedded Jetty", e);
        }
    }

    @Override
    public synchronized void stop() throws EmbeddedServletContainerException {
        try {
            this.server.stop();
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        } catch (Exception ex) {
            throw new EmbeddedServletContainerException(
                    "Unable to stop embedded Jetty servlet container", ex);
        }
    }

    @Override
    public int getPort() {
        Connector[] connectors = this.server.getConnectors();
        for (Connector connector : connectors) {
            // Probably only one...
            return getLocalPort(connector);
        }
        return 0;
    }

    private void handleDeferredInitialize(Handler... handlers) throws Exception {
        for (Handler handler : handlers) {
            if (handler instanceof JettyEmbeddedWebAppContext) {
                ((JettyEmbeddedWebAppContext) handler).deferredInitialize();
            }
            else if (handler instanceof HandlerWrapper) {
                handleDeferredInitialize(((HandlerWrapper) handler).getHandler());
            }
            else if (handler instanceof HandlerCollection) {
                handleDeferredInitialize(((HandlerCollection) handler).getHandlers());
            }
        }
    }

    private String getActualPortsDescription() {
        StringBuilder ports = new StringBuilder();
        for (Connector connector : this.server.getConnectors()) {
            ports.append(ports.length() == 0 ? "" : ", ");
            ports.append(getLocalPort(connector) + getProtocols(connector));
        }
        return ports.toString();
    }

    private Integer getLocalPort(Connector connector) {
        try {
            // Jetty 9 internals are different, but the method name is the same
            return (Integer) ReflectionUtils.invokeMethod(
                    ReflectionUtils.findMethod(connector.getClass(), "getLocalPort"),
                    connector);
        }
        catch (Exception ex) {
            JettyEmbeddedServletContainer.logger.info("could not determine port ( "
                    + ex.getMessage() + ")");
            return 0;
        }
    }

    private String getProtocols(Connector connector) {
        try {
            List<String> protocols = connector.getProtocols();
            return " (" + StringUtils.collectionToDelimitedString(protocols, ", ") + ")";
        }
        catch (NoSuchMethodError ex) {
            // Not available with Jetty 8
            return "";
        }

    }
}
