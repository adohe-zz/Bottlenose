package com.xqbase.baiji.exceptions;

/**
 * Base Baiji Runtime Exception.
 *
 * @author Tony He
 */
public class BaijiRuntimeException extends RuntimeException {

    public BaijiRuntimeException(String message) {
        super(message);
    }

    public BaijiRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public BaijiRuntimeException(Throwable cause) {
        super(cause);
    }
}
