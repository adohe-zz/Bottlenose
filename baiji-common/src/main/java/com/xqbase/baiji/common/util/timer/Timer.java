package com.xqbase.baiji.common.util.timer;

import java.util.concurrent.TimeUnit;

/**
 * Monitor type for tracking how much time something is taking.
 *
 * @author Tony He
 */
public interface Timer {

    /**
     * Returns a stopwatch that has been started and will automatically
     * record its result to this timer when stopped.
     */
    Stopwatch start();

    /**
     * The time unit reported by this timer.
     */
    TimeUnit getTimeUnit();

    /**
     * Record a new value that was collected with the given TimeUnit.
     */
    void record(long duration, TimeUnit timeUnit);
}
