package com.xqbase.baiji.loadbalancer.stats;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.xqbase.baiji.loadbalancer.Server;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Capture various stats per Server in the LoadBalancer
 *
 * @author Tony He
 */
public class ServerStats {

    private static final int DEFAULT_PUBLISH_INTERVAL =  60 * 1000; // = 1 minute
    private static final int DEFAULT_BUFFER_SIZE = 60 * 1000; // = 1000 requests/sec for 1 minute
    private final DynamicIntProperty connectionFailureThreshold;

    private static final double[] PERCENTS = makePercentValues();

    private DataDistribution dataDist = new DataDistribution(1, PERCENTS); // in case
    private DataPublisher publisher = null;
    private final Distribution responseTimeDist = new Distribution();

    private int bufferSize = DEFAULT_BUFFER_SIZE;
    private int publishInterval = DEFAULT_PUBLISH_INTERVAL;

    private Server server;

    private AtomicLong totalRequests = new AtomicLong();
    private AtomicInteger successiveConnectionFailureCount = new AtomicInteger(0);
    private AtomicInteger activeRequestsCount = new AtomicInteger(0);

    public ServerStats() {
        connectionFailureThreshold = DynamicPropertyFactory.getInstance().getIntProperty(
                "baiji.loadbalancer.default.conntion.failure.threshold", 3);
    }

    public void initialize(final Server server) {
        if (null == publisher) {
            dataDist = new DataDistribution(getBufferSize(), PERCENTS);
            publisher = new DataPublisher(dataDist, getPublishIntervalMillis());
            publisher.start();
        }
        this.server = server;
    }

    private int getBufferSize() {
        return bufferSize;
    }

    private long getPublishIntervalMillis() {
        return publishInterval;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public void setPublishInterval(int publishInterval) {
        this.publishInterval = publishInterval;
    }

    /**
     * The supported percentile values.
     * These correspond to the various Monitor methods defined below.
     * No, this is not pretty, but that's the way it is.
     */
    private static enum Percent {

        TEN(10), TWENTY_FIVE(25), FIFTY(50), SEVENTY_FIVE(75), NINETY(90),
        NINETY_FIVE(95), NINETY_EIGHT(98), NINETY_NINE(99), NINETY_NINE_POINT_FIVE(99.5);

        private double val;

        Percent(double val) {
            this.val = val;
        }

        public double getValue() {
            return val;
        }

    }

    private static double[] makePercentValues() {
        Percent[] percents = Percent.values();
        double[] p = new double[percents.length];
        for (int i = 0; i < percents.length; i++) {
            p[i] = percents[i].getValue();
        }
        return p;
    }
}
