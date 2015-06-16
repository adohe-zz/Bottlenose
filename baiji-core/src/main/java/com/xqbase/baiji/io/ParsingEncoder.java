package com.xqbase.baiji.io;

import com.xqbase.baiji.exceptions.BaijiTypeException;

import java.io.IOException;
import java.util.Arrays;

/**
 * Base class for any parser based encoder.
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

    @Override
    public void setItemCount(long itemCount) throws IOException {
        if (counts[pos] != 0) {
            throw new BaijiTypeException("Incorrect number of items written. " + counts[pos] + " more required");
        }
        counts[pos] = itemCount;
    }

    @Override
    public void startItem() throws IOException {
        counts[pos] --;
    }

    /**
     * Push a new collection on to the stack.
     */
    protected final void push() {
        if (++pos == counts.length) {
            counts = Arrays.copyOf(counts, pos + 10);
        }
        counts[pos] = 0;
    }

    /**
     * Pop
     */
    protected final void pop() {
        if (counts[pos] != 0) {
            throw new BaijiTypeException("Incorrect number of items written. " + counts[pos] + " more required");
        }
        pos --;
    }

    protected final int depth() {
        return pos;
    }
}
