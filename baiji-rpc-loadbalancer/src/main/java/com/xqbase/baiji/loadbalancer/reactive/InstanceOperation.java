package com.xqbase.baiji.loadbalancer.reactive;

import com.ctriposs.baiji.rpc.client.loadbalancer.Instance;
import rx.Observable;
import rx.functions.Func1;

/**
 * Provide the {@link rx.Observable} for a specified server. Used
 * by {@link com.ctriposs.baiji.rpc.client.loadbalancer.reactive.LoadBalancerCommand}
 *
 * @param <T> Output type
 */
public interface InstanceOperation<T> extends Func1<Instance, Observable<T>> {

    /**
     * @return A lazy {@link Observable} for the server supplied. It is expected
     * that the actual execution is not started until the returned {@link Observable} is subscribed to.
     */
    @Override
    Observable<T> call(Instance instance);
}
