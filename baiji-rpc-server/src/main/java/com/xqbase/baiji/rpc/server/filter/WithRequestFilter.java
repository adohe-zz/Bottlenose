package com.xqbase.baiji.rpc.server.filter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Define a RequestFilter annotation which enables request filter.
 *
 * @author Tony He
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface WithRequestFilter {

    /**
     * The class of the request filter.
     */
    Class<? extends RequestFilter> value();

    /**
     * The execution priority of the filter.
     * <p/>
     * Filters will be executed in the ascending order of priority.
     * <p/>
     * Filters with a negative priority will be executed before all global request filters.
     * Filters with a non-negative priority will be executed before all global request filters.
     */
    int priority() default 0;

    /**
     * Whether the filter instance can be reused.
     */
    boolean reuse() default true;
}
