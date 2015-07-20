package com.xqbase.baiji.loadbalancer.collector;

/**
 * Accumulator of statistics about a distribution of
 * observed values that are produced incrementally.
 * <p>
 * Note that the implementation is <em>not</em> synchronized,
 * and simultaneous updates may produce incorrect results.
 * In most cases these incorrect results will be unimportant,
 * but applications that care should synchronize carefully
 * to ensure consistent results.
 * <p>
 *
 */
public class Distribution implements DataCollector {

    private long numValues;
    private double sumValues;
    private double sumSquareValues;
    private double minValue;
    private double maxValue;

    /**
     * Creates a new initially empty Distribution.
     */
    public Distribution() {
        numValues = 0L;
        sumValues = 0.0;
        sumSquareValues = 0.0;
        minValue = 0.0;
        maxValue = 0.0;
    }

    /*
     * Accumulating new values
     */
    public void noteValue(double val) {
        numValues++;
        sumValues += val;
        sumSquareValues += val * val;
        if (numValues == 1) {
            minValue = val;
            maxValue = val;
        } else if (val < minValue) {
            minValue = val;
        } else if (val > maxValue) {
            maxValue = val;
        }
    }

    public void clear() {
        numValues = 0L;
        sumValues = 0.0;
        sumSquareValues = 0.0;
        minValue = 0.0;
        maxValue = 0.0;
    }

    /*
     * Accessors
     */

    public long getNumValues() {
        return numValues;
    }

    public double getMean() {
        if (numValues < 1) {
            return 0.0;
        } else {
            return sumValues / numValues;
        }
    }

    public double getVariance() {
        if (numValues < 2) {
            return 0.0;
        } else if (sumValues == 0.0) {
            return 0.0;
        } else {
            double mean = getMean();
            return (sumSquareValues / numValues) - mean * mean;
        }
    }

    public double getStdDev() {
        return Math.sqrt(getVariance());
    }

    public double getMinimum() {
        return minValue;
    }

    public double getMaximum() {
        return maxValue;
    }

    /**
     * Add another {@link Distribution}'s values to this one.
     *
     * @param anotherDistribution
     *            the other {@link Distribution} instance
     */
    public void add(Distribution anotherDistribution) {
        if (anotherDistribution != null) {
            numValues += anotherDistribution.numValues;
            sumValues += anotherDistribution.sumValues;
            sumSquareValues += anotherDistribution.sumSquareValues;
            minValue = (minValue < anotherDistribution.minValue) ? minValue
                    : anotherDistribution.minValue;
            maxValue = (maxValue > anotherDistribution.maxValue) ? maxValue
                    : anotherDistribution.maxValue;
        }
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{Distribution:")
                .append("N=").append(getNumValues())
                .append(": ").append(getMinimum())
                .append("..").append(getMean())
                .append("..").append(getMaximum())
                .append("}")
                .toString();
    }
}
