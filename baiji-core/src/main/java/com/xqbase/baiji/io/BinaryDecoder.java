package com.xqbase.baiji.io;

import com.xqbase.baiji.exceptions.BaijiRuntimeException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * An {@link Decoder} for binary-format data.
 * <p/>
 * This class may read-ahead and buffer bytes from the source beyond what is
 * required to serve its read methods.
 *
 * @see Encoder
 */

public class BinaryDecoder implements Decoder {

    private static final int ONE_MINUTE = 1000 * 60;

    private final InputStream _stream;

    public BinaryDecoder(InputStream stream) {
        _stream = stream;
    }

    @Override
    public void readNull() throws IOException {
    }

    @Override
    public boolean readBoolean() throws IOException {
        byte b = read();
        if (b == 0) {
            return false;
        }
        if (b == 1) {
            return true;
        }
        throw new BaijiRuntimeException("Not a boolean value in the stream: " + b);
    }

    @Override
    public int readInt() throws IOException {
        return (int) readLong();
    }

    @Override
    public long readLong() throws IOException {
        byte b = read();
        long n = b & 0x7FL;
        int shift = 7;
        while ((b & 0x80) != 0) {
            b = read();
            n |= (b & 0x7FL) << shift;
            shift += 7;
        }
        long value = n;
        return (-(value & 0x01L)) ^ ((value >> 1) & 0x7fffffffffffffffL);
    }

    @Override
    public float readFloat() throws IOException {
        int bits = (_stream.read() & 0xff) |
                (_stream.read() & 0xff) << 8 |
                (_stream.read() & 0xff) << 16 |
                (_stream.read() & 0xff) << 24;
        return Float.intBitsToFloat(bits);
    }

    @Override
    public double readDouble() throws IOException {
        long bits = (_stream.read() & 0xffL) |
                (_stream.read() & 0xffL) << 8 |
                (_stream.read() & 0xffL) << 16 |
                (_stream.read() & 0xffL) << 24 |
                (_stream.read() & 0xffL) << 32 |
                (_stream.read() & 0xffL) << 40 |
                (_stream.read() & 0xffL) << 48 |
                (_stream.read() & 0xffL) << 56;
        return Double.longBitsToDouble(bits);
    }

    @Override
    public byte[] readBytes() throws IOException {
        return read(readLong());
    }

    @Override
    public String readString() throws IOException {
        int length = readInt();
        byte[] buffer = new byte[length];
        read(buffer, 0, length);
        return new String(buffer, 0, length, "utf-8");
    }

    @Override
    public Calendar readDatetime() throws IOException {
        long value = readLong();
        long offset = readLong() * ONE_MINUTE;
        TimeZone timeZone = new SimpleTimeZone((int) offset, "");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.setTimeInMillis(value);
        return calendar;
    }

    @Override
    public int readEnum() throws IOException {
        return readInt();
    }

    @Override
    public long readArrayStart() throws IOException {
        return doReadItemCount();
    }

    @Override
    public long readArrayNext() throws IOException {
        return doReadItemCount();
    }

    @Override
    public long readMapStart() throws IOException {
        return doReadItemCount();
    }

    @Override
    public long readMapNext() throws IOException {
        return doReadItemCount();
    }

    @Override
    public int readUnionIndex() throws IOException {
        return readInt();
    }

    // read p bytes into a new byte buffer
    private byte[] read(long p) throws IOException {
        return read((int) p);
    }

    private byte[] read(int p) throws IOException {
        byte[] buffer = new byte[p];
        read(buffer, 0, buffer.length);
        return buffer;
    }

    private byte read() throws IOException {
        int n = _stream.read();
        if (n >= 0) {
            return (byte) n;
        }
        throw new BaijiRuntimeException("End of stream reached");
    }

    private void read(byte[] buffer, int start, int len) throws IOException {
        while (len > 0) {
            int n = _stream.read(buffer, start, len);
            if (n <= 0) {
                throw new BaijiRuntimeException("End of stream reached");
            }
            start += n;
            len -= n;
        }
    }

    private long doReadItemCount() throws IOException {
        long result = readLong();
        if (result < 0) {
            readLong(); // Consume byte-count if present
            result = -result;
        }
        return result;
    }
}
