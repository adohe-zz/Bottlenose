package com.xqbase.bn.transport.bridge.common;

/**
 * Transport Client callback.
 *
 * @author Tony He
 */
public interface TransportCallback<T> {

    void onResponse(TransportResponse<T> response);
}
