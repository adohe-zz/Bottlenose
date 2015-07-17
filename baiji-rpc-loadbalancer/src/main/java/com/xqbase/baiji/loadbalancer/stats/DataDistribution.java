package com.xqbase.baiji.loadbalancer.stats;

import java.util.Date;

/**
 * A {@link DataAccumulator} that also publishes statistics about the "previous" buffer.
 *
 */
public class DataDistribution extends DataAccumulator {

    private long numValues = 0L;
    private double mean = 0.0;
    private double variance = 0.0;
    private double stddev = 0.0;
    private double min = 0.0;
    private double max = 0.0;
    private long ts = 0L;
    private long interval = 0L;
    private int size = 0;
    private final double[] percents;
    private final double[] percentiles;

    /**
     * Creates a new DataDistribution with no data summarized.
     *
     * @param bufferSize the size of each buffer held by the {@code DataAccumulator}
     * @param percents array of percentile values to calculate when buffers
     *    are swapped and new data is published.
     *    The array values must be in the range {@code [0 .. 100]}.
     */
    public DataDistribution(int bufferSize, double[] percents) {
        super(bufferSize);
        assert percentsOK(percents);
        this.percents = percents;
        this.percentiles = new double[percents.length];
    }

    private static boolean percentsOK(double[] percents) {
        if (percents == null) {
            return false;
        }
        for (int i = 0; i < percents.length; i++) {
            if (percents[i] < 0.0 || percents[i] > 100.0) { // SUPPRESS CHECKSTYLE MagicNumber
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    protected void publish(DataBuffer buf) {
        ts = System.currentTimeMillis();
        numValues = buf.getNumValues();
        mean = buf.getMean();
        variance = buf.getVariance();
        stddev = buf.getStdDev();
        min = buf.getMinimum();
        max = buf.getMaximum();
        interval = buf.getSampleIntervalMillis();
        size = buf.getSampleSize();
        buf.getPercentiles(percents, percentiles);
    }

    /*
     * DataDistributionMBean protocol
     */

    /** {@inheritDoc} */
    public void clear() {
        numValues = 0L;
        mean = 0.0;
        variance = 0.0;
        stddev = 0.0;
        min = 0.0;
        max = 0.0;
        ts = 0L;
        interval = 0L;
        size = 0;
        for (int i = 0; i < percentiles.length; i++) {
            percentiles[i] = 0.0;
        }
    }

    public long getNumValues() {
        return numValues;
    }

    public double getMean() {
        return mean;
    }

    public double getVariance() {
        return variance;
    }

    public double getStdDev() {
        return stddev;
    }

    public double getMinimum() {
        return min;
    }

    public double getMaximum() {
        return max;
    }

    public String getTimestamp() {
        return new Date(getTimestampMillis()).toString();
    }

    public long getTimestampMillis() {
        return ts;
    }

    public long getSampleIntervalMillis() {
        return interval;
    }

    public int getSampleSize() {
        return size;
    }

    public double[] getPercents() {
        return percents;
    }

    public double[] getPercentiles() {
        return percentiles;
    }
}
