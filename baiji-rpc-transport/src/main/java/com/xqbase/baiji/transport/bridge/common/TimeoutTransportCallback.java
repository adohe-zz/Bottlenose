package com.xqbase.baiji.transport.bridge.common;

import com.xqbase.baiji.common.timeout.Timeout;
import com.xqbase.baiji.common.timeout.TimeoutExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * A transport callback wrapper associates with timeout.
 *
 * @author Tony He
 */
public class TimeoutTransportCallback<T> implements TransportCallback<T>, TimeoutExecutor {

    private final Timeout<TransportCallback<T>> timeout;

    public TimeoutTransportCallback(ScheduledExecutorService scheduler, final ExecutorService executor,
                                    long t, TimeUnit timeUnit, final TransportCallback<T> callback) {
        timeout = new Timeout<>(scheduler, t, timeUnit, callback);
        timeout.addTimeoutTask(new Runnable() {
            @Override
            public void run() {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        // timeout happened
                    }
                });
            }
        });
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onResponse(TransportResponse<T> response) {
        TransportCallback callback = timeout.getItem();
        if (callback != null) {
            callback.onResponse(response);
        }
    }

    @Override
    public void addTimeoutTask(Runnable task) {
        timeout.addTimeoutTask(task);
    }
}
