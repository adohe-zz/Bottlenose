package com.xqbase.baiji.loadbalancer.stats;

import com.netflix.config.DynamicIntProperty;
import com.netflix.config.DynamicPropertyFactory;
import com.xqbase.baiji.loadbalancer.Server;
import com.xqbase.baiji.loadbalancer.collector.DataDistribution;
import com.xqbase.baiji.loadbalancer.collector.DataPublisher;
import com.xqbase.baiji.loadbalancer.collector.Distribution;

import java.util.Date;
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
    private static final DynamicIntProperty activeRequestsCountTimeout =
            DynamicPropertyFactory.getInstance().getIntProperty("baiji.loadbalancer.serverStats.activeRequestsCount.effectiveWindowSeconds", 60 * 10);

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

    private volatile long lastConnectionFailedTimestamp;
    private volatile long lastActiveRequestsCountChangeTimestamp;
    private AtomicLong totalCircuitBreakerBlackOutPeriod = new AtomicLong(0);
    private volatile long lastAccessedTimestamp;
    private volatile long firstConnectionTimestamp = 0;

    public ServerStats() {
        connectionFailureThreshold = DynamicPropertyFactory.getInstance().getIntProperty(
                "baiji.loadbalancer.default.connection.failure.threshold", 3);
    }

    public void initialize(final Server server) {
        if (null == publisher) {
            dataDist = new DataDistribution(getBufferSize(), PERCENTS);
            publisher = new DataPublisher(dataDist, getPublishIntervalMillis());
            publisher.start();
        }
        this.server = server;
    }

    public void close() {
        if (publisher != null) {
            publisher.stop();
        }
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

    /**
     * Call this method to note the response time after every request
     */
    public void noteResponseTime(double msc){
        dataDist.noteValue(msc);
        responseTimeDist.noteValue(msc);
    }

    public void incrementActiveRequestsCount() {
        activeRequestsCount.incrementAndGet();
        long currentTime = System.currentTimeMillis();
        lastActiveRequestsCountChangeTimestamp = currentTime;
        lastAccessedTimestamp = currentTime;
        if (firstConnectionTimestamp == 0) {
            firstConnectionTimestamp = currentTime;
        }
    }

    public void decrementActiveRequestsCount() {
        if (activeRequestsCount.decrementAndGet() < 0) {
            activeRequestsCount.set(0);
        }
        lastActiveRequestsCountChangeTimestamp = System.currentTimeMillis();
    }

    public int getActiveRequestsCount() {
        return getActiveRequestsCount(System.currentTimeMillis());
    }

    private int getActiveRequestsCount(long currentTime) {
        int count = activeRequestsCount.get();
        if (count == 0) {
            return 0;
        } else if (currentTime - lastActiveRequestsCountChangeTimestamp > activeRequestsCountTimeout.get() * 1000 || count < 0) {
            activeRequestsCount.set(0);
            return 0;
        } else {
            return count;
        }
    }

    public void incrementNumRequests() {
        totalRequests.incrementAndGet();
    }

    public void incrementSuccessiveConnectionFailureCount() {
        successiveConnectionFailureCount.incrementAndGet();
        lastConnectionFailedTimestamp = System.currentTimeMillis();
    }

    public void clearSuccessiveConnectionFailureCount() {
        successiveConnectionFailureCount.set(0);
    }

    public int getSuccessiveConnectionFailureCount() {
        return successiveConnectionFailureCount.get();
    }

    /**
     * Gets the average total amount of time to handle a request, in milliseconds.
     */
    public double getResponseTimeAvg() {
        return responseTimeDist.getMean();
    }

    /**
     * Gets the maximum amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTimeMax() {
        return responseTimeDist.getMaximum();
    }

    /**
     * Gets the minimum amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTimeMin() {
        return responseTimeDist.getMinimum();
    }

    /**
     * Gets the standard deviation in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTimeStdDev() {
        return responseTimeDist.getStdDev();
    }

    /**
     * Gets the average total amount of time to handle a request
     * in the recent time-slice, in milliseconds.
     */
    public double getResponseTimeAvgRecent() {
        return dataDist.getMean();
    }

    /**
     * Gets the 10-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime10thPercentile() {
        return getResponseTimePercentile(Percent.TEN);
    }

    /**
     * Gets the 25-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime25thPercentile() {
        return getResponseTimePercentile(Percent.TWENTY_FIVE);
    }

    /**
     * Gets the 50-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime50thPercentile() {
        return getResponseTimePercentile(Percent.FIFTY);
    }

    /**
     * Gets the 75-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime75thPercentile() {
        return getResponseTimePercentile(Percent.SEVENTY_FIVE);
    }

    /**
     * Gets the 90-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime90thPercentile() {
        return getResponseTimePercentile(Percent.NINETY);
    }

    /**
     * Gets the 95-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime95thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_FIVE);
    }

    /**
     * Gets the 98-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime98thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_EIGHT);
    }

    /**
     * Gets the 99-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime99thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_NINE);
    }

    /**
     * Gets the 99.5-th percentile in the total amount of time spent handling a request, in milliseconds.
     */
    public double getResponseTime99point5thPercentile() {
        return getResponseTimePercentile(Percent.NINETY_NINE_POINT_FIVE);
    }

    public long getTotalRequestsCount() {
        return totalRequests.get();
    }

    private double getResponseTimePercentile(Percent p) {
        return dataDist.getPercentiles()[p.ordinal()];
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        sb.append("[Server:" + server + ";");
        sb.append("\tTotal Requests:" + totalRequests + ";");
        sb.append("\tSuccessive connection failure:" + getSuccessiveConnectionFailureCount() + ";");
        sb.append("\tTotal blackout seconds:" + totalCircuitBreakerBlackOutPeriod.get() / 1000 + ";");
        sb.append("\tLast connection made:" + new Date(lastAccessedTimestamp) + ";");
        if (lastConnectionFailedTimestamp > 0) {
            sb.append("\tLast connection failure: " + new Date(lastConnectionFailedTimestamp)  + ";");
        }
        sb.append("\tFirst connection made: " + new Date(firstConnectionTimestamp)  + ";");
        sb.append("\tActive connections:" + getActiveRequestsCount() + ";");
        sb.append("\taverage resp time:" + getResponseTimeAvg()  + ";");
        sb.append("\t90 percentile resp time:" + getResponseTime90thPercentile()  + ";");
        sb.append("\t95 percentile resp time:" + getResponseTime95thPercentile()  + ";");
        sb.append("\tmin resp time:" + getResponseTimeMin()  + ";");
        sb.append("\tmax resp time:" + getResponseTimeMax()  + ";");
        sb.append("\tstddev resp time:" + getResponseTimeStdDev());
        sb.append("]\n");

        return sb.toString();
    }
}
