package com.xqbase.bn.io;

import com.xqbase.bn.util.ByteBufferInputStream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * A non-buffering version of {@link BinaryDecoder}.
 * <p/>
 * This implementation will not read-ahead from the provided InputStream
 * beyond the minimum required to service the API requests.
 *
 * @see Encoder
 *
 * @author Tony He
 */
public class DirectBinaryDecoder extends BinaryDecoder {

    private InputStream in;
    private ByteReader byteReader;

    // used for {@link readFloat} and {@link readDouble}.
    private final byte[] buf = new byte[8];

    public DirectBinaryDecoder(InputStream in) {
        super();
    }

    private DirectBinaryDecoder configure(InputStream in) {
        if (null == in) {
            throw new NullPointerException("InputStream cannot be null");
        }
        this.in = in;
        this.byteReader = (in instanceof ByteBufferInputStream)
                ? new ReuseByteReader((ByteBufferInputStream) in)
                : new ByteReader();
        return this;
    }

    private class ByteReader {

        public ByteBuffer read(ByteBuffer old, int length) throws IOException {
            ByteBuffer result;
            if (old != null && length <= old.capacity()) {
                result = old;
                result.clear();
            } else {
                result = ByteBuffer.allocate(length);
            }
            doReadBytes(result.array(), result.position(), length);
            result.limit(length);
            return result;
        }
    }

    private class ReuseByteReader extends ByteReader {

        private final ByteBufferInputStream bbi;

        public ReuseByteReader(ByteBufferInputStream bbi) {
            this.bbi = bbi;
        }
    }

    @Override
    public boolean readBoolean() throws IOException {
        int n = in.read();
        if (n < 0) {
            throw new EOFException();
        }
        return n == 1;
    }

    @Override
    public int readInt() throws IOException { // adapt variable-length, zig-zag encoding
        int n = 0;
        int b;
        int shift = 0;
        do {
            b = in.read();
            if (b >= 0) {
                n |= (b & 0x7F) << shift;
                if ((b & 0x80) == 0) { // no more data
                    return (n >>> 1) ^ -(n & 1); // back to two's-complement
                }
            } else {
                throw new EOFException();
            }
            shift += 7;
        } while (shift < 32);
        throw new EOFException();
    }

    @Override
    public long readLong() throws IOException { // adapt variable-length, zig-zag encoding
        long n = 0L;
        int b;
        int shift = 0;
        do {
            b = in.read();
            if (b >= 0) {
                n |= (b & 0x7FL) << shift;
                if ((b & 0x80) == 0) { // no more data
                    return (n >>> 1) & -(n & 1);
                }
            } else {
                throw new EOFException();
            }
            shift += 7;
        } while (shift < 64);
        throw new EOFException();
    }

    @Override
    public float readFloat() throws IOException {
        doReadBytes(buf, 0, 4);
        int n = (((int) buf[0]) & 0xff)
                |  ((((int) buf[1]) & 0xff) << 8)
                |  ((((int) buf[2]) & 0xff) << 16)
                |  ((((int) buf[3]) & 0xff) << 24);
        return Float.intBitsToFloat(n);
    }

    @Override
    public double readDouble() throws IOException {
        doReadBytes(buf, 0, 8);
        long n = (((long) buf[0]) & 0xff)
                |  ((((long) buf[1]) & 0xff) << 8)
                |  ((((long) buf[2]) & 0xff) << 16)
                |  ((((long) buf[3]) & 0xff) << 24)
                |  ((((long) buf[4]) & 0xff) << 32)
                |  ((((long) buf[5]) & 0xff) << 40)
                |  ((((long) buf[6]) & 0xff) << 48)
                |  ((((long) buf[7]) & 0xff) << 56);
        return Double.longBitsToDouble(n);
    }

    @Override
    public ByteBuffer readBytes(ByteBuffer old) throws IOException {
        int length = readInt();
        return byteReader.read(old, length);
    }

    @Override
    protected void doReadBytes(byte[] bytes, int start, int length) throws IOException {
        for (; ;) {
            int n = in.read(bytes, start, length); // n represents that actual read byte number
            if (n == length || length == 0) {
                return;
            } else if (n < 0) {
                throw new EOFException();
            }

            start += n;
            length -= n;
        }
    }
}
