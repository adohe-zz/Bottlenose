package com.xqbase.baiji.io;

import java.io.IOException;
import java.io.OutputStream;
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

    private OutputStream out;
    // the buffer is used for writing floats, doubles, and large longs.
    private final byte[] buf = new byte[12];

    public DirectBinaryEncoder(OutputStream out) {
        if (null == out)
            throw new NullPointerException("OutputStream cannot be null!");
        this.out = out;
    }

    @Override
    protected void writeZero() throws IOException {
        out.write(0);
    }

    @Override
    public int bytesBuffered() {
        return 0;
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        out.write(b ? 1 : 0);
    }

    /**
     * Buffering is slower for int that encode to just 1 or
     * two bytes, and are faster for large ones.
     * @throws IOException
     */
    @Override
    public void writeInt(int n) throws IOException { // variable length, zigzag encoding
        // move sign to low-order bit, and flip others if negative
        int val = (n << 1) ^ (n >> 31);
        if ((val & ~0x7F) == 0) {
            out.write(val);
            return;
        } else if ((val & ~0x3FFF) == 0) {
            out.write(0x80 | val);
            out.write(val >>> 7);
            return;
        }
        int len = BinaryData.encodeInt(n, buf, 0);
        out.write(buf, 0, len);
    }

    /**
     * Buffering is slower for writeLong when the number is small enough to
     * fit in an int.
     * @throws IOException
     */
    @Override
    public void writeLong(long n) throws IOException {
        // move sign to low-order bit
        long val = (n << 1) ^ (n >> 63);
        if ((val & ~0x7FFFFFFFL) == 0) {
            int i = (int) val;
            while ((i & ~0x7F) != 0) {
                out.write((byte)((0x80 | i) & 0xFF));
                i >>>= 7;
            }
            out.write((byte)i);
            return;
        }
        int len = BinaryData.encodeLong(n, buf, 0);
        out.write(buf, 0, len);
    }

    /**
     * Buffering is faster for writeFloat.
     * @throws IOException
     */
    @Override
    public void writeFloat(float f) throws IOException {
        // a float is written as four bytes
        int len = BinaryData.encodeFloat(f, buf, 0);
        out.write(buf, 0, len);
    }

    /**
     * Buffering is faster for writeDouble.
     * @throws IOException
     */
    @Override
    public void writeDouble(double d) throws IOException {
        // a double is written as eight bytes
        byte[] buf = new byte[8];
        int len = BinaryData.encodeDouble(d, buf, 0);
        out.write(buf, 0, len);
    }

    @Override
    public void writeFixed(byte[] bytes, int start, int len) throws IOException {
        out.write(bytes, start, len);
    }

    @Override
    public void writeDatetime(Calendar date) throws IOException {
        // a date is written as long type
        writeLong(date.getTimeInMillis());
    }

    @Override
    public void flush() throws IOException {
        out.flush();
    }
}
