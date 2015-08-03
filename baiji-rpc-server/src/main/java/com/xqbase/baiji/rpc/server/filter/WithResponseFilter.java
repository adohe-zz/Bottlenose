package com.xqbase.baiji.rpc.server.filter;

/**
 * Define a ResponseFilter annotation which enables response filter.
 *
 * @author Tony He
 */
public @interface WithResponseFilter {

    /**
     * The class of the response filter.
     */
    Class<? extends ResponseFilter> value();

    /**
     * The execution priority of the filter.
     * <p/>
     * Filters will be executed in the ascending order of priority.
     * <p/>
     * Filters with a negative priority will be executed before all global response filters.
     * Filters with a non-negative priority will be executed before all global response filters.
     */
    int priority() default 0;

    /**
     * Whether the filter instance can be reused.
     */
    boolean reuse() default true;
}
