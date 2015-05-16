package com.xqbase.baiji.common.callback;

/**
 * A callback provides a means for user to get notification when
 * an asynchronous operation finished.
 *
 * @author Tony He
 */
public interface Callback<T> extends SuccessCallback<T> {

    void onError(Throwable e);
}
