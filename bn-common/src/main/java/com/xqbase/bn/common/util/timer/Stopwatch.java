package com.xqbase.bn.common.util.timer;

import java.util.concurrent.TimeUnit;

/**
 * Measures the time taken for execution of some code.
 *
 * @author Tony He
 */
public interface Stopwatch {

    /** Mark the start time. */
    void start();

    /** Mark the end time. */
    void stop();

    /** Reset the stopwatch so that it can be used again. */
    void reset();

    /** Returns the duration in the specified time unit. */
    long getDuration(TimeUnit timeUnit);

    /** Returns the duration in nanoseconds. */
    long getDuration();
}
