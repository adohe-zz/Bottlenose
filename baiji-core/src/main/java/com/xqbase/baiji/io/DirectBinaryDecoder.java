package com.xqbase.baiji.io;

import com.xqbase.baiji.util.ByteBufferInputStream;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

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
        return super.readFloat();
    }

    @Override
    public double readDouble() throws IOException {
        return super.readDouble();
    }

    @Override
    public byte[] readBytes() throws IOException {
        return super.readBytes();
    }
}
