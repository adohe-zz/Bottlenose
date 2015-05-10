package com.xqbase.baiji.io;

/**
 * Utilities for binary-encoded data.
 */
public class BinaryData {

    private BinaryData() {
    }

    /** Encode an integer to the byte array at the given position. Will throw
     * IndexOutOfBounds if it overflows. Users should ensure that there are at
     * least 5 bytes left in the buffer before calling this method.
     *
     * @return The number of bytes written to the buffer, between 1 and 5.
     */
    public static int encodeInt(int n, byte[] buf, int pos) {
        // move sign to low-order bit, and flip others if negative
        n = (n << 1) ^ (n >> 31);
        int start = pos;
        if ((n & ~0x7F) != 0) {
            buf[pos++] = (byte)((n | 0x80) & 0xFF);
            n >>>= 7;
            if (n > 0x7F) {
                buf[pos++] = (byte)((n | 0x80) & 0xFF);
                n >>>= 7;
                if (n > 0x7F) {
                    buf[pos++] = (byte)((n | 0x80) & 0xFF);
                    n >>>= 7;
                    if (n > 0x7F) {
                        buf[pos++] = (byte)((n | 0x80) & 0xFF);
                        n >>>= 7;
                    }
                }
            }
        }
        buf[pos++] = (byte) n;
        return pos - start;
    }

    /** Encode a long to the byte array at the given position. Will throw
     * IndexOutOfBounds if it overflows. Users should ensure that there are at
     * least 10 bytes left in the buffer before calling this method.
     *
     * @return The number of bytes written to the buffer, between 1 and 10.
     */
    public static int encodeLong(long n, byte[] buf, int pos) {
        // move sign to low-order bit, and flip others if negative
        n = (n << 1) ^ (n >> 63);
        int start = pos;
        if ((n & ~0x7FL) != 0) {
            buf[pos++] = (byte)((n | 0x80) & 0xFF);
            n >>>= 7;
            if (n > 0x7F) {
                buf[pos++] = (byte)((n | 0x80) & 0xFF);
                n >>>= 7;
                if (n > 0x7F) {
                    buf[pos++] = (byte)((n | 0x80) & 0xFF);
                    n >>>= 7;
                    if (n > 0x7F) {
                        buf[pos++] = (byte)((n | 0x80) & 0xFF);
                        n >>>= 7;
                        if (n > 0x7F) {
                            buf[pos++] = (byte)((n | 0x80) & 0xFF);
                            n >>>= 7;
                            if (n > 0x7F) {
                                buf[pos++] = (byte)((n | 0x80) & 0xFF);
                                n >>>= 7;
                                if (n > 0x7F) {
                                    buf[pos++] = (byte)((n | 0x80) & 0xFF);
                                    n >>>= 7;
                                    if (n > 0x7F) {
                                        buf[pos++] = (byte)((n | 0x80) & 0xFF);
                                        n >>>= 7;
                                        if (n > 0x7F) {
                                            buf[pos++] = (byte)((n | 0x80) & 0xFF);
                                            n >>>= 7;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        buf[pos++] = (byte) n;
        return pos - start;
    }
}
