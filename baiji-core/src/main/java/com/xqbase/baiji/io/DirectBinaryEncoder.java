package com.xqbase.baiji.io;

import java.io.IOException;
import java.util.Calendar;

/**
 * An {@link Encoder} for Baiji's binary encoding that does not buffer output.
 * <p/>
 * This encoder does not buffer writes, and as a result is slower than
 * {@link BufferedBinaryEncoder}. However, it is lighter-weight and useful when the
 * buffering in BufferedBinaryEncoder is not desired and/or the Encoder is
 * very short lived.
 * <p/>
 *
 * @author Tony He
 */
public class DirectBinaryEncoder extends BinaryEncoder {
    
    @Override
    protected void writeZero() throws IOException {

    }

    @Override
    public int bytesBuffered() {
        return 0;
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {

    }

    @Override
    public void writeInt(int n) throws IOException {

    }

    @Override
    public void writeLong(long n) throws IOException {

    }

    @Override
    public void writeFloat(float f) throws IOException {

    }

    @Override
    public void writeDouble(double d) throws IOException {

    }

    @Override
    public void writeBytes(byte[] bytes) throws IOException {

    }

    @Override
    public void writeDatetime(Calendar date) throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }
}
