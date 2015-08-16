package com.xqbase.bn.loadbalancer.collector;

/**
 * An object that collects new values incrementally.
 *
 */
public interface DataCollector {

    /**
     * Adds a value to the collected data.
     * This must run very quickly, and so can safely
     * be called in time-critical code.
     */
    void noteValue(double val);

}
