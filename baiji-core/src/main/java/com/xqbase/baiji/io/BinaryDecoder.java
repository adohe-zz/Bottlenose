package com.xqbase.baiji.io;

import java.io.IOException;
import java.util.Calendar;

/**
 * An {@link Decoder} for binary-format data.
 * <p/>
 * This class may read-ahead and buffer bytes from the source beyond what is
 * required to serve its read methods.
 * The number of unused bytes in the buffer can be accessed by
 * inputStream().remaining(), if the BinaryDecoder is not 'direct'.
 *
 * @see Encoder
 */

public class BinaryDecoder implements Decoder {

    protected BinaryDecoder() {}

    @Override
    public void readNull() throws IOException {

    }

    @Override
    public boolean readBoolean() throws IOException {
        return false;
    }

    @Override
    public int readInt() throws IOException {
        return 0;
    }

    @Override
    public long readLong() throws IOException {
        return 0;
    }

    @Override
    public float readFloat() throws IOException {
        return 0;
    }

    @Override
    public double readDouble() throws IOException {
        return 0;
    }

    @Override
    public byte[] readBytes() throws IOException {
        return new byte[0];
    }

    /**
     * Reads <tt>length</tt> bytes into <tt>bytes</tt> starting at <tt>start</tt>.
     *
     * @throws java.io.EOFException
     *                  If there are not enough number of bytes in the source.
     * @throws java.io.IOException
     */
    protected void doReadBytes(byte[] bytes, int start, int length) throws IOException {

    }

    @Override
    public String readString() throws IOException {
        return null;
    }

    @Override
    public Calendar readDatetime() throws IOException {
        return null;
    }

    @Override
    public int readEnum() throws IOException {
        return 0;
    }

    @Override
    public long readArrayStart() throws IOException {
        return 0;
    }

    @Override
    public long readArrayNext() throws IOException {
        return 0;
    }

    @Override
    public long readMapStart() throws IOException {
        return 0;
    }

    @Override
    public long readMapNext() throws IOException {
        return 0;
    }

    @Override
    public int readUnionIndex() throws IOException {
        return 0;
    }
}
