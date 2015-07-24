package com.xqbase.baiji.common.util.timer;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * This class does not enforce starting or stopping once and only once without a reset.
 *
 * @author Tony He
 */
public class BasicStopwatch implements Stopwatch {

    private AtomicLong startTime = new AtomicLong(0L);
    private AtomicLong stopTime = new AtomicLong(0L);
    private AtomicBoolean running = new AtomicBoolean(false);

    /** Create a BasicStopwatch without associate timer. */
    public BasicStopwatch() {
    }

    @Override
    public void start() {
        startTime.set(System.nanoTime());
        running.set(true);
    }

    @Override
    public void stop() {
        stopTime.set(System.nanoTime());
        running.set(false);
    }

    @Override
    public void reset() {
        startTime.set(0L);
        stopTime.set(0L);
        running.set(false);
    }

    @Override
    public long getDuration(TimeUnit timeUnit) {
        return timeUnit.convert(getDuration(), TimeUnit.NANOSECONDS);
    }

    /**
     * Returns the duration in nanoseconds. No checks are performed to ensure that the stopwatch
     * has been properly started and stopped before executing this method. If called before stop
     * it will return the current duration.
     */
    @Override
    public long getDuration() {
        final long stop = running.get() ? System.nanoTime() : stopTime.get();
        return stop - startTime.get();
    }
}
