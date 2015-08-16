package com.xqbase.bn.rpc.server.tomcat;

/**
 * Exceptions thrown by an embedded servlet container.
 *
 * @author Tony He
 */
public class EmbeddedServletContainerException extends RuntimeException {

    public EmbeddedServletContainerException(String message, Throwable cause) {
        super(message, cause);
    }
}
