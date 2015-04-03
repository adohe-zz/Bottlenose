package com.xqbase.baiji.exceptions;


/**
 * Thrown when an illegal type is used.
 */
public class BaijiTypeException extends BaijiRuntimeException {
    public BaijiTypeException(String message) {
        super(message);
    }

    public BaijiTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}

