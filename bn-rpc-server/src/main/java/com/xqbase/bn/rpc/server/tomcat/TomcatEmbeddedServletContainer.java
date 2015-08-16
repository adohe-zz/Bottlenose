package com.xqbase.bn.rpc.server.tomcat;

import com.xqbase.bn.common.logging.Logger;
import com.xqbase.bn.common.logging.LoggerFactory;
import org.apache.catalina.Engine;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@link EmbeddedServletContainer} that can be used to control an embedded Tomcat server.
 *
 * @author Tony He
 */
public class TomcatEmbeddedServletContainer implements EmbeddedServletContainer {

    private static final Logger logger = LoggerFactory.getLogger(TomcatEmbeddedServletContainer.class);

    private final AtomicInteger containerCounter = new AtomicInteger(-1);

    private final Tomcat tomcat;
    private final boolean autoStart;

    private final Map<Service, Connector[]> serviceConnectors = new HashMap<Service, Connector[]>();

    public TomcatEmbeddedServletContainer(final Tomcat tomcat) {
        this(tomcat, true);
    }

    public TomcatEmbeddedServletContainer(final Tomcat tomcat, boolean autoStart) {
        this.tomcat = tomcat;
        this.autoStart = autoStart;
        initialize();
    }

    private synchronized void initialize() throws EmbeddedServletContainerException {
        TomcatEmbeddedServletContainer.logger.info("Tomcat initialized with port(s): "
                + getPortsDescription(false));
        try {
            addInstanceIdToEngineName();

            // Remove service connectors to that protocol binding doesn't happen yet
            removeServiceConnectors();

            // Start the server to trigger initialization listeners
            this.tomcat.start();

            // Unlike Jetty, all Tomcat threads are daemon threads. We create a
            // blocking non-daemon to stop immediate shutdown
            startDaemonAwaitThread();

        } catch (Exception ex) {
            throw new EmbeddedServletContainerException("Unable to start embedded tomcat", ex);
        }
    }

    private void addInstanceIdToEngineName() {
        int instanceId = containerCounter.incrementAndGet();
        if (instanceId > 0) {
            Engine engine = this.tomcat.getEngine();
            engine.setName(engine.getName() + "-" + instanceId);
        }
    }

    private void removeServiceConnectors() {
        Service[] services = this.tomcat.getServer().findServices();
        for (Service service : services) {
            Connector[] connectors = service.findConnectors().clone();
            this.serviceConnectors.put(service, connectors);
            for (Connector connector : connectors) {
                service.removeConnector(connector);
            }
        }
    }

    @Override
    public void start() throws EmbeddedServletContainerException {
        addPreviouslyRemovedConnectors();
        Connector connector = this.tomcat.getConnector();
        if (connector != null && this.autoStart) {
            startConnector(connector);
        }
        // Ensure process isn't left running if it actually failed to start
        if (connectorsHaveFailedToStart()) {
            stopSilently();
            throw new IllegalStateException("Tomcat connector in failed state");
        }
        TomcatEmbeddedServletContainer.logger.info("Tomcat started on port(s): "
                + getPortsDescription(true));
    }

    private void startConnector(Connector connector) {
    }

    private void stopSilently() {
        try {
            this.tomcat.stop();
        }
        catch (LifecycleException ex) {
        }
    }

    private boolean connectorsHaveFailedToStart() {
        for (Connector connector : this.tomcat.getService().findConnectors()) {
            if (LifecycleState.FAILED.equals(connector.getState())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void stop() throws EmbeddedServletContainerException {
        try {
            try {
                this.tomcat.stop();
                this.tomcat.destroy();
            } catch (LifecycleException ex) {
                // swallow and continue
            }
        } catch (Exception ex) {
            throw new EmbeddedServletContainerException("Unable to stop embedded Tomcat",
                    ex);
        } finally {
            containerCounter.decrementAndGet();
        }
    }

    @Override
    public int getPort() {
        Connector connector = this.tomcat.getConnector();
        if (connector != null) {
            return connector.getLocalPort();
        }
        return 0;
    }

    private void addPreviouslyRemovedConnectors() {
        Service[] services = this.tomcat.getServer().findServices();
        for (Service service : services) {
            Connector[] connectors = this.serviceConnectors.get(service);
            if (connectors != null) {
                for (Connector connector : connectors) {
                    service.addConnector(connector);
                    if (!this.autoStart) {
                        stopProtocolHandler(connector);
                    }
                }
                this.serviceConnectors.remove(service);
            }
        }
    }

    private void stopProtocolHandler(Connector connector) {
        try {
            connector.getProtocolHandler().stop();
        }
        catch (Exception ex) {
            TomcatEmbeddedServletContainer.logger.error("Cannot pause connector: ", ex);
        }
    }

    private void startDaemonAwaitThread() {
        Thread awaitThread = new Thread("container-" + (containerCounter.get())) {

            @Override
            public void run() {
                TomcatEmbeddedServletContainer.this.tomcat.getServer().await();
            }

        };
        awaitThread.setDaemon(false);
        awaitThread.start();
    }

    private String getPortsDescription(boolean localPort) {
        StringBuilder ports = new StringBuilder();
        for (Connector connector : this.tomcat.getService().findConnectors()) {
            ports.append(ports.length() == 0 ? "" : " ");
            int port = (localPort ? connector.getLocalPort() : connector.getPort());
            ports.append(port + " (" + connector.getScheme() + ")");
        }
        return ports.toString();
    }
}
