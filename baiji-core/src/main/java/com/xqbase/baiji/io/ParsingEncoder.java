package com.xqbase.baiji.io;

/**
 * Base class for parser based encoder.
 *
 * @author Tony He
 */
public abstract class ParsingEncoder implements Encoder {

    /**
     * Tracks the number of items that remain to be written in
     * the collections (array or map).
     */
    private long[] counts = new long[10];

    protected int pos = -1;
}
