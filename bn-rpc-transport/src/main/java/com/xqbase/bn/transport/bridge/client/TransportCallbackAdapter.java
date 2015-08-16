package com.xqbase.bn.transport.bridge.client;

import com.xqbase.bn.common.callback.Callback;
import com.xqbase.bn.transport.bridge.common.TransportCallback;
import com.xqbase.bn.transport.bridge.common.TransportResponse;

/**
 * The TransportCallbackAdapter.
 *
 * @author Tony He
 */
public class TransportCallbackAdapter<T> implements TransportCallback<T> {

    private Callback<T> callback;

    public TransportCallbackAdapter(Callback<T> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(TransportResponse<T> response) {
        if (response.hasError()) {
            callback.onError(response.getError());
            return;
        }

        callback.onSuccess(response.getResponse());
    }
}
