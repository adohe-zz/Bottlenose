package com.xqbase.bn.loadbalancer.reactive;

import com.xqbase.bn.loadbalancer.Server;
import rx.Observable;
import rx.functions.Func1;

/**
 * Provide the {@link rx.Observable} for a specified server. Used
 * by {@link com.xqbase.bn.loadbalancer.reactive.LoadBalancerCommand}
 *
 * @param <T> Output type
 */
public interface ServerOperation<T> extends Func1<Server, Observable<T>> {

    /**
     * @return A lazy {@link Observable} for the server supplied. It is expected
     * that the actual execution is not started until the returned {@link Observable} is subscribed to.
     */
    @Override
    Observable<T> call(Server server);
}
