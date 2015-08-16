package com.xqbase.bn.transport.bridge.common;

/**
 * An object that represents the actual response or error.
 *
 * @author Tony He
 */
public interface TransportResponse<T> {

    T getResponse();

    boolean hasError();

    Throwable getError();
}
