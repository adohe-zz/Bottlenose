package com.xqbase.baiji.io;

import com.xqbase.baiji.exceptions.BaijiTypeException;
import com.xqbase.baiji.util.Utf8;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * Low-level support for de-serializing Baiji values.
 * <p/>
 * This class has two types of methods.  One type of methods support
 * the reading of leaf values (for example, {@link #readLong} and
 * {@link #readString}).
 * <p/>
 * The other type of methods support the reading of maps and arrays.
 * These methods are {@link #readArrayStart}, {@link #readArrayNext},
 * and similar methods for maps).  See {@link #readArrayStart} for
 * details on these methods.)
 * <p/>
 *
 * @see Encoder
 */

public interface Decoder {

    /**
     * "Reads" a null value.  (Doesn't actually read anything, but
     * advances the state of the parser if the implementation is
     * stateful.)
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            null is not the type of the next value to be read
     */
    void readNull() throws IOException;

    /**
     * Reads a boolean value written by {@link Encoder#writeBoolean}.
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            boolean is not the type of the next value to be read
     */

    boolean readBoolean() throws IOException;

    /**
     * Reads an integer written by {@link Encoder#writeInt}.
     *
     * @throws BaijiTypeException If encoded value is larger than
     *                            32-bits
     * @throws BaijiTypeException If this is a stateful reader and
     *                            int is not the type of the next value to be read
     */
    int readInt() throws IOException;

    /**
     * Reads a long written by {@link Encoder#writeLong}.
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            long is not the type of the next value to be read
     */
    long readLong() throws IOException;

    /**
     * Reads a float written by {@link Encoder#writeFloat}.
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            is not the type of the next value to be read
     */
    float readFloat() throws IOException;

    /**
     * Reads a double written by {@link Encoder#writeDouble}.
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            is not the type of the next value to be read
     */
    double readDouble() throws IOException;

    /**
     * Reads a byte-string written by {@link Encoder#writeBytes}.
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            byte-string is not the type of the next value to be read
     */
    byte[] readBytes() throws IOException;

    /**
     * Reads a byte-string written by {@link Encoder#writeBytes}.
     * if <tt>old</tt> is not null and has sufficient capacity to take in
     * the bytes being read, the bytes are returned in <tt>old</tt>.
     * @throws BaijiTypeException If this is a stateful reader and
     *              byte-string is not the type of the next value to be read.
     */
    ByteBuffer readBytes(ByteBuffer old) throws IOException;

    /**
     * Reads fixed sized binary object.
     * @param bytes The buffer to store the contents being read.
     * @param start The position where the data need to be written.
     * @param length The size of the binary object.
     * @throws BaijiTypeException If this is a stateful read and
     *          the fixed size binary object is not the type of the next
     *          value to be read or the length is incorrect.
     */
    void readFixed(byte[] bytes, int start, int length) throws IOException;

    /**
     * A shorthand of <tt>readFixed(bytes, 0, bytes.length)</tt>.
     * @param bytes The buffer to store the contents being read.
     * @throws BaijiTypeException If this is a stateful read and
     *          the fixed size binary object is not the type of the next
     *          value to be read or the length is incorrect.
     */
    void readFixed(byte[] bytes) throws IOException;

    /**
     * Reads a char-string written by {@link Encoder#writeString}.
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            char-string is not the type of the next value to be read
     */
    String readString(Utf8 old) throws IOException;

    /**
     * Reads a char-string written by {@link Encoder#writeString}.
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            char-string is not the type of the next value to be read
     */
    String readString() throws IOException;

    /**
     * Reads a date time written by {@link Encoder#writeDatetime(java.util.Calendar)}
     *
     * @return a date time
     * @throws BaijiTypeException If this is a stateful reader and
     *                            date time is not the type of the next value to be read
     */
    Calendar readDatetime() throws IOException;

    /**
     * Reads an enumeration.
     *
     * @return The enumeration's value.
     * @throws BaijiTypeException  If this is a stateful reader and
     *                             enumeration is not the type of the next value to be read.
     * @throws java.io.IOException
     */
    int readEnum() throws IOException;

    /**
     * Reads and returns the size of the first block of an array.  If
     * this method returns non-zero, then the caller should read the
     * indicated number of items, and then call {@link
     * #readArrayNext} to find out the number of items in the next
     * block.  The typical pattern for consuming an array looks like:
     * <pre>
     *   for(long i = in.readArrayStart(); i != 0; i = in.arrayNext()) {
     *     for (long j = 0; j < i; j++) {
     *       read next element of the array;
     *     }
     *   }
     * </pre>
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            array is not the type of the next value to be read
     */
    long readArrayStart() throws IOException;

    /**
     * Processes the next block of an array and returns the number of items in
     * the block and let's the caller
     * read those items.
     *
     * @throws BaijiTypeException When called outside of an
     *                            array context
     */
    long readArrayNext() throws IOException;

    /**
     * Reads and returns the size of the next block of map-entries.
     * Similar to {@link #readArrayStart}.
     * <p/>
     * As an example, let's say you want to read a map of records,
     * the record consisting of an Long field and a Boolean field.
     * Your code would look something like this:
     * <pre>
     *   Map<String,Record> m = new HashMap<String,Record>();
     *   Record reuse = new Record();
     *   for(long i = in.readMapStart(); i != 0; i = in.readMapNext()) {
     *     for (long j = 0; j < i; j++) {
     *       String key = in.readString();
     *       reuse.intField = in.readInt();
     *       reuse.boolField = in.readBoolean();
     *       m.put(key, reuse);
     *     }
     *   }
     * </pre>
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            map is not the type of the next value to be read
     */
    long readMapStart() throws IOException;

    /**
     * Processes the next block of map entries and returns the count of them.
     * Similar to {@link #readArrayNext}.  See {@link #readMapStart} for details.
     *
     * @throws BaijiTypeException When called outside of a
     *                            map context
     */
    long readMapNext() throws IOException;

    /**
     * Reads the tag of a union written by {@link Encoder#writeUnionIndex}.
     *
     * @throws BaijiTypeException If this is a stateful reader and
     *                            union is not the type of the next value to be read
     */
    int readUnionIndex() throws IOException;
}

