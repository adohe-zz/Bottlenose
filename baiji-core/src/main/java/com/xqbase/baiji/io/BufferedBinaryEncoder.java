package com.xqbase.baiji.io;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Calendar;

/**
 * A {@link Encoder} for Baiji's binary encoding.
 * <p/>
 * This implementation buffers output to enhance performance.
 * Output may not appear on the underlying output until flush() is called.
 *
 * @see Encoder
 * @see Decoder
 *
 * @author Tony He
 */
public class BufferedBinaryEncoder extends BinaryEncoder {

    private byte[] buf;
    private int pos;
    private ByteSink sink;
    private int bulkLimit;

    public BufferedBinaryEncoder(OutputStream out, int bufferSize) {
        configure(out, bufferSize);
    }

    BufferedBinaryEncoder configure(OutputStream out, int bufferSize) {
        if (null == out) {
            throw new NullPointerException("OutputStream cannot be null");
        }

        if (this.sink != null) {

        }

        this.sink = new OutputStreamSink(out);
        pos = 0;
        if (null == buf || buf.length != bufferSize) {
            buf = new byte[bufferSize];
        }
        bulkLimit = buf.length >>> 1;
        if (bulkLimit > 512) {
            bulkLimit = 512;
        }
        return this;
    }

    /**
     * Flushes the internal buffer to the underlying output.
     * Does not flush the underlying output.
     */
    private void flushBuffer() throws IOException {
        if (pos > 0) {
            sink.innerWrite(buf, 0, pos);
            pos = 0;
        }
    }

    /**
     * Ensures that the buffer has at least num bytes free to write to between its
     * current position and the end. This will not expand the buffer larger than
     * its current size, for writes larger than or near to the size of the buffer,
     * we flush the buffer and write directly to the output, bypassing the buffer.
     * @param num number of bytes free
     * @throws IOException
     */
    private void ensureBounds(int num) throws IOException {
        int remaining = buf.length - pos;
        if (remaining < num) {
            flushBuffer();
        }
    }

    @Override
    protected void writeZero() throws IOException {

    }

    @Override
    public int bytesBuffered() {
        return 0;
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        if (buf.length == pos) {
            flushBuffer();
        }
    }

    @Override
    public void writeInt(int n) throws IOException {
        ensureBounds(5);
        pos += BinaryData.encodeInt(n, buf, pos);
    }

    @Override
    public void writeLong(long n) throws IOException {
        ensureBounds(10);
        pos += BinaryData.encodeLong(n, buf, pos);
    }

    @Override
    public void writeFloat(float f) throws IOException {
        ensureBounds(4);
        pos += BinaryData.encodeFloat(f, buf, pos);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        ensureBounds(8);
        pos += BinaryData.encodeDouble(d, buf, pos);
    }

    @Override
    public void writeFixed(byte[] bytes, int start, int len) throws IOException {

    }

    @Override
    public void writeDatetime(Calendar date) throws IOException {

    }

    @Override
    public void flush() throws IOException {
        flushBuffer();
        sink.innerFlush();
    }

    /**
     * ByteSink abstracts the destination of written data from the core workings
     * of BinaryEncoder.
     */
    private abstract static class ByteSink {

        protected ByteSink() {}

        /** Write data from bytes, starting at off, for len bytes. */
        protected abstract void innerWrite(byte[] bytes, int off, int len) throws IOException;

        protected abstract void innerWrite(ByteBuffer buffer) throws IOException;

        /** Flush the underlying output. */
        protected abstract void innerFlush() throws IOException;
    }

    static class OutputStreamSink extends ByteSink {

        private final OutputStream out;
        private final WritableByteChannel channel;

        private OutputStreamSink(OutputStream out) {
            super();
            this.out = out;
            this.channel = Channels.newChannel(out);
        }

        @Override
        protected void innerWrite(byte[] bytes, int off, int len) throws IOException {
            out.write(bytes, off, len);
        }

        @Override
        protected void innerWrite(ByteBuffer buffer) throws IOException {
            channel.write(buffer);
        }

        @Override
        protected void innerFlush() throws IOException {
            out.flush();
        }
    }
}

