package com.xqbase.baiji.transport.bridge.client;

import com.xqbase.baiji.common.callback.Callback;
import com.xqbase.baiji.transport.bridge.common.TransportCallback;
import com.xqbase.baiji.transport.bridge.common.TransportResponse;

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
