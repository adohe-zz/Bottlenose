package com.xqbase.baiji.rpc.server.jetty;

import com.xqbase.baiji.common.util.StringUtils;
import com.xqbase.baiji.rpc.server.tomcat.EmbeddedServletContainer;
import com.xqbase.baiji.rpc.server.tomcat.EmbeddedServletContainerFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.File;
import java.net.InetAddress;

/**
 * {@link EmbeddedServletContainerFactory} that can be used to create
 * {@link JettyEmbeddedServletContainer}s. Can be initialized using
 * Tomcat {@link org.apache.catalina.LifecycleListener}s.
 * <p>
 * Unless explicitly configured otherwise this factory will created containers that
 * listens for HTTP requests on port 8080.
 *
 * @author Tony He
 */
public class JettyEmbeddedServletContainerFactory implements EmbeddedServletContainerFactory {

    private int port = DEFAULT_PORT;
    private String contextPath = DEFAULT_CONTEXT_PATH;

    private InetAddress address;

    @Override
    public EmbeddedServletContainer getEmbeddedServletContainer() {
        ServletContextHandler handler = new ServletContextHandler();
        int port = (getPort() > 0) ? getPort() : 0;
        Server server = new Server(port);
        configureServletContextHandler(handler);
        server.setHandler(handler);
        return getJettyEmbeddedServletContainer(server);
    }

    protected final void configureServletContextHandler(ServletContextHandler handler) {
        handler.setContextPath("/");
        addBaijiServlet(handler);
    }

    /**
     * Add BaijiServlet to the given {@link org.eclipse.jetty.servlet.ServletContextHandler}.
     */
    protected final void addBaijiServlet(ServletContextHandler handler) {
        ServletHolder holder = new ServletHolder();
        holder.setName("default");
        holder.setClassName("com.xqbase.baiji.rpc.server.BaijiServlet");
        holder.setInitParameter("dirAllowed", "false");
        holder.setInitOrder(1);
        handler.addServlet(holder, "/*");
    }

    private JettyEmbeddedServletContainer getJettyEmbeddedServletContainer(Server server) {
        return new JettyEmbeddedServletContainer(server, getPort() > 0);
    }

    private File getTempDirectory() {
        String temp = System.getProperty("java.io.tmpdir");
        return (temp == null ? null : new File(temp));
    }

    public int getPort() {
        return port;
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void setContextPath(String contextPath) {
        this.contextPath = contextPath;
    }

    public String getContextPath() {
        return contextPath;
    }

    public InetAddress getAddress() {
        return address;
    }
}
