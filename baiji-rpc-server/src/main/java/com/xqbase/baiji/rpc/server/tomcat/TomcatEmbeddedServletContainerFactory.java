package com.xqbase.baiji.rpc.server.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Host;
import org.apache.catalina.Wrapper;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.startup.Tomcat;
import org.apache.coyote.AbstractProtocol;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;

/**
 * {@link EmbeddedServletContainerFactory} that can be used to create
 * {@link TomcatEmbeddedServletContainer}s. Can be initialized using
 * Tomcat {@link org.apache.catalina.LifecycleListener}s.
 * <p>
 * Unless explicitly configured otherwise this factory will created containers that
 * listens for HTTP requests on port 8080.
 *
 * @author Tony He
 */
public class TomcatEmbeddedServletContainerFactory implements EmbeddedServletContainerFactory {

    private static final String DEFAULT_PROTOCOL = "org.apache.coyote.http11.Http11NioProtocol";

    private File baseDirectory;

    private String protocol = DEFAULT_PROTOCOL;
    private int port = DEFAULT_PORT;
    private String uriEncoding = "UTF-8";
    private String contextPath = DEFAULT_CONTEXT_PATH;

    private InetAddress address;

    @Override
    public EmbeddedServletContainer getEmbeddedServletContainer() {
        Tomcat tomcat = new Tomcat();
        File baseDir = (this.baseDirectory != null ? this.baseDirectory
                : createTempDir("tomcat"));
        tomcat.setBaseDir(baseDir.getAbsolutePath());
        Connector connector = new Connector(this.protocol);
        tomcat.getService().addConnector(connector);
        customizeConnector(connector);
        tomcat.setConnector(connector);
        tomcat.getHost().setAutoDeploy(false);
        tomcat.getEngine().setBackgroundProcessorDelay(-1);
        prepareContext(tomcat.getHost());
        return getTomcatEmbeddedServletContainer(tomcat);
    }

    // Needs to be protected so it can be used by subclasses
    protected void customizeConnector(Connector connector) {
        int port = (getPort() >= 0 ? getPort() : 0);
        connector.setPort(port);
        if (connector.getProtocolHandler() instanceof AbstractProtocol) {
            if (getAddress() != null) {
                ((AbstractProtocol<?>) connector.getProtocolHandler())
                        .setAddress(getAddress());
            }
        }
        if (getUriEncoding() != null) {
            connector.setURIEncoding(getUriEncoding());
        }

        // If ApplicationContext is slow to start we want Tomcat not to bind to the socket
        // prematurely...
        connector.setProperty("bindOnInit", "false");
    }

    private void prepareContext(Host host) {
        TomcatEmbeddedContext context = new TomcatEmbeddedContext();
        File docBase = createTempDir("tomcat-docbase");
        context.setName("Baiji");
        context.setPath("");
        context.setDocBase(docBase.getAbsolutePath());
        context.addLifecycleListener(new Tomcat.FixContextListener());
        addDefaultServlet(context);
        host.addChild(context);
    }

    private void addDefaultServlet(Context context) {
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName("default");
        defaultServlet.setServletClass("com.xqbase.baiji.rpc.server.BaijiServlet");
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        defaultServlet.setOverridable(true);
        context.addChild(defaultServlet);
        context.addServletMapping("/*", "default");
    }

    private File createTempDir(String prefix) {
        try {
            File tempFolder = File.createTempFile(prefix + ".", "." + getPort());
            tempFolder.delete();
            tempFolder.mkdir();
            tempFolder.deleteOnExit();
            return tempFolder;
        }
        catch (IOException ex) {
            throw new EmbeddedServletContainerException(
                    "Unable to create Tomcat tempdir. java.io.tmpdir is set to "
                            + System.getProperty("java.io.tmpdir"), ex);
        }
    }

    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    public String getContextPath() {
        return contextPath;
    }

    @Override
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getUriEncoding() {
        return uriEncoding;
    }

    public void setUriEncoding(String uriEncoding) {
        this.uriEncoding = uriEncoding;
    }

    public InetAddress getAddress() {
        return address;
    }

    /**
     * Factory method called to create the {@link TomcatEmbeddedServletContainer}.
     * Subclasses can override this method to return a different
     * {@link TomcatEmbeddedServletContainer} or apply additional processing to the Tomcat
     * server.
     * @param tomcat the Tomcat server.
     * @return a new {@link TomcatEmbeddedServletContainer} instance
     */
    protected TomcatEmbeddedServletContainer getTomcatEmbeddedServletContainer(
            Tomcat tomcat) {
        return new TomcatEmbeddedServletContainer(tomcat, getPort() >= 0);
    }

    public void setBaseDirectory(final File baseDirectory) {
        this.baseDirectory = baseDirectory;
    }
}
