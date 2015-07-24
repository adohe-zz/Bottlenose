package com.xqbase.baiji.common.util.timer;

import java.util.concurrent.TimeUnit;

/**
 * A simple timer implementation providing the total time, count, min, and max for the times that
 * have been recorded.
 *
 * @author Tony He
 */
public class BasicTimer implements Timer {

    private final String name;
    private final TimeUnit timeUnit;

    public BasicTimer(String name, TimeUnit timeUnit) {
        this.name = name;
        this.timeUnit = timeUnit;
    }

    @Override
    public Stopwatch start() {
        BasicStopwatch s = new BasicStopwatch();
        s.start();
        return s;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    @Override
    public void record(long duration, TimeUnit timeUnit) {

    }
}
