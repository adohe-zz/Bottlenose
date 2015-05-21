package com.xqbase.baiji.transport.http.common;

import com.xqbase.baiji.transport.bridge.common.TransportCallback;
import com.xqbase.baiji.transport.bridge.common.TransportResponse;

/**
 * A transport callback wrapper associates with timeout.
 *
 * @author Tony He
 */
public class TimeoutTransportCallback<T> implements TransportCallback<T> {

    @Override
    public void onResponse(TransportResponse<T> response) {

    }
}
