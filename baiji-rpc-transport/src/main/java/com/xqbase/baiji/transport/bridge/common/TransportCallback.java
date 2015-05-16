package com.xqbase.baiji.transport.bridge.common;

/**
 * Transport Client callback.
 *
 * @author Tony He
 */
public interface TransportCallback<T> {

    void onResponse(TransportResponse<T> response);
}
