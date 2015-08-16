package com.xqbase.bn.loadbalancer.reactive;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A context object that is created at start of each load balancer execution
 * and contains certain meta data of the load balancer and mutable state data of
 * execution per listener per request. Each listener will get its own context
 * to work with. But it can also call {@link ExecutionContext#getGlobalContext()} to
 * get the shared context between all listeners.
 *
 * @author Tony He
 *
 */
public class ExecutionContext<T> {

    private final Map<String, Object> context;
    private final ConcurrentHashMap<Object, ChildContext<T>> subContexts;
    private final T request;

    private static class ChildContext<T> extends ExecutionContext<T> {
        private final ExecutionContext<T> parent;

        ChildContext(ExecutionContext<T> parent) {
            super(parent.request);
            this.parent = parent;
        }

        @Override
        public ExecutionContext<T> getGlobalContext() {
            return parent;
        }
    }

    public ExecutionContext(T request) {
        this.request = request;
        this.context = new HashMap<>();
        this.subContexts = new ConcurrentHashMap<>();
    }

    public ExecutionContext<T> getChildContext(Object obj) {

        ChildContext<T> subContext = subContexts.get(obj);
        if (null == subContext) {
            subContext = new ChildContext<>(this);
            ChildContext<T> old = subContexts.putIfAbsent(obj, subContext);
            if (old != null) {
                subContext = old;
            }
        }

        return subContext;
    }

    public T getRequest() {
        return request;
    }

    public Object get(String name) {
        return context.get(name);
    }

    public void put(String name, Object value) {
        context.put(name, value);
    }

    /**
     *
     * @return The shared context for all listeners.
     */
    public ExecutionContext<T> getGlobalContext() {
        return this;
    }
}
