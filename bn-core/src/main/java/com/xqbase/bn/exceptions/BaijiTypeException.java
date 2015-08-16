package com.xqbase.bn.exceptions;


/**
 * Thrown when an illegal type is used.
 *
 * @author Tony He
 */
public class BaijiTypeException extends BaijiRuntimeException {

    public BaijiTypeException(String message) {
        super(message);
    }

    public BaijiTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}

