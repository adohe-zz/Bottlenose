package com.xqbase.bn.io;

import com.xqbase.bn.util.Utf8;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * An abstract {@link Encoder} for Baiji's binary encoding.
 *
 * @author Tony He
 */
public abstract class BinaryEncoder implements Encoder {

    @Override
    public void writeNull() throws IOException {}

    @Override
    public void writeString(Utf8 utf8) throws IOException {
        this.writeBytes(utf8.getBytes(), 0, utf8.getByteLength());
    }

    @Override
    public void writeString(String str) throws IOException {
        writeString(new Utf8(str));
    }

    @Override
    public void writeString(CharSequence charSequence) throws IOException {
        if (charSequence instanceof Utf8) {
            writeString((Utf8) charSequence);
        } else {
            writeString(charSequence.toString());
        }
    }

    @Override
    public void writeBytes(byte[] bytes, int start, int len) throws IOException {
        if (0 == len) {
            writeZero();
            return;
        }
        this.writeInt(len);
        this.writeFixed(bytes, start, len);
    }

    /**
     * Writes a byte string.
     * Equivalent to <tt>writeBytes(bytes, 0, bytes.length)</tt>
     * @throws IOException
     * @throws com.xqbase.bn.exceptions.BaijiTypeException If
     * this is a stateful writer and a byte-string is not expected
     */
    public void writeBytes(byte[] bytes) throws IOException {
        writeBytes(bytes, 0, bytes.length);
    }

    @Override
    public void writeBytes(ByteBuffer bytes) throws IOException {
        int len = bytes.limit() - bytes.remaining();
        if (len == 0) {
            writeZero();
        } else {
            writeInt(len);
            int pos = bytes.position();
            int l = bytes.limit() - pos;
            if (bytes.hasArray()) {
                writeFixed(bytes.array(), bytes.arrayOffset() + pos, l);
            } else {
                byte[] b = new byte[l];
                bytes.duplicate().get(b, 0, l);
                writeFixed(b, 0, l);
            }
        }
    }

    @Override
    public void writeEnum(int e) throws IOException {
        this.writeInt(e);
    }

    @Override
    public void writeArrayStart() throws IOException {}

    @Override
    public void setItemCount(long itemCount) throws IOException {
        if (itemCount > 0) {
            this.writeLong(itemCount);
        }
    }

    @Override
    public void startItem() throws IOException {}

    @Override
    public void writeArrayEnd() throws IOException {
        writeZero();
    }

    @Override
    public void writeMapStart() throws IOException {}

    @Override
    public void writeMapEnd() throws IOException {
        writeZero();
    }

    @Override
    public void writeUnionIndex(int unionIndex) throws IOException {
        writeInt(unionIndex);
    }

    /** Write a zero byte to the underlying output. **/
    protected abstract void writeZero() throws IOException;

    /**
     * Returns the number of bytes currently buffered by this encoder. If this
     * Encoder does not buffer, this will always return zero.
     * <p/>
     * Call {@link #flush()} to empty the buffer to the underlying output.
     */
    public abstract int bytesBuffered();
}
