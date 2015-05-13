package com.xqbase.baiji.io;

import com.xqbase.baiji.exceptions.BaijiTypeException;
import com.xqbase.baiji.util.Utf8;

import java.io.Flushable;
import java.io.IOException;
import java.util.Calendar;

/**
 * Low-level support for serializing Baiji values.
 * <p/>
 * This class has two types of methods.  One type of methods support
 * the writing of leaf values (for example, {@link #writeLong} and
 * {@link #writeString}).  These methods have analogs in {@link
 * Decoder}.
 * <p/>
 * The other type of methods support the writing of maps and arrays.
 * These methods are {@link #writeArrayStart}, {@link
 * #startItem}, and {@link #writeArrayEnd} (and similar methods for
 * maps).  Some implementations of {@link Encoder} handle the
 * buffering required to break large maps and arrays into blocks,
 * which is necessary for applications that want to do streaming.
 * (See {@link #writeArrayStart} for details on these methods.)
 * <p/>
 *
 * @see Decoder
 */
public interface Encoder extends Flushable {

    /**
     * "Writes" a null value.  (Doesn't actually write anything, but
     * advances the state of the parser if this class is stateful.)
     *
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            null is not expected
     */
    void writeNull() throws IOException;

    /**
     * Write a boolean value.
     *
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            boolean is not expected
     */
    void writeBoolean(boolean b) throws IOException;

    /**
     * Writes a 32-bit integer.
     *
     * @throws BaijiTypeException If this is a stateful writer and an
     *                            integer is not expected
     */
    void writeInt(int n) throws IOException;

    /**
     * Write a 64-bit integer.
     *
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            long is not expected
     */
    void writeLong(long n) throws IOException;

    /**
     * Write a float.
     *
     * @throws java.io.IOException
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            float is not expected
     */
    void writeFloat(float f) throws IOException;

    /**
     * Write a double.
     *
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            double is not expected
     */
    void writeDouble(double d) throws IOException;

    /**
     * Write a Unicode character string.  The default implementation converts
     * the String to a {@link com.xqbase.baiji.util.Utf8}.  Some Encoder
     * implementations may want to do something different as a performance optimization.
     *
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            char-string is not expected
     */
    void writeString(Utf8 str) throws IOException;

    /**
     * Write a Unicode character string.  The default implementation converts
     * the String to a {@link com.xqbase.baiji.util.Utf8}.  Some Encoder
     * implementations may want to do something different as a performance optimization.
     * @throws BaijiTypeException If this is a stateful writer and a
     * char-string is not expected
     */
    void writeString(String str) throws IOException;

    /**
     * Write a Unicode character string. If the CharSequence is an
     * {@link com.xqbase.baiji.util.Utf8} it writes it directly, otherwise
     * the CharSequence is converted to a String via toString() and written.
     * @throws BaijiTypeException If this is a stateful writer and a
     * char-string is not expected
     */
    void writeString(CharSequence charSequence) throws IOException;

    /**
     * Write a byte string.
     * @throws BaijiTypeException If this is a stateful writer and a
     * byte-string is not expected
     */
    void writeBytes(byte[] bytes, int start, int len) throws IOException;

    /**
     * Writes a fixed size binary object.
     * @param bytes The contents to write
     * @param start The position within <tt>bytes</tt> where the contents
     * start.
     * @param len The number of bytes to write.
     * @throws BaijiTypeException If this is a stateful writer and a
     * byte-string is not expected
     * @throws IOException
     */
    void writeFixed(byte[] bytes, int start, int len) throws IOException;

    /**
     * Write a date
     * @param date the date
     * @throws java.io.IOException
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            date is not expected
     */
    void writeDatetime(Calendar date) throws IOException;

    /**
     * Writes an enumeration.
     *
     * @param e
     * @throws BaijiTypeException If this is a stateful writer and an enumeration
     *                            is not expected or the <tt>e</tt> is out of range.
     * @throws java.io.IOException
     */
    void writeEnum(int e) throws IOException;

    /**
     * Call this method to start writing an array.
     * <p/>
     * When starting to serialize an array, call {@link
     * #writeArrayStart}. Then, before writing any data for any item
     * call {@link #setItemCount} followed by a sequence of
     * {@link #startItem()} and the item itself. The number of
     * {@link #startItem()} should match the number specified in
     * {@link #setItemCount}.
     * When actually writing the data of the item, you can call any {@link
     * Encoder} method (e.g., {@link #writeLong}).  When all items
     * of the array have been written, call {@link #writeArrayEnd}.
     * <p/>
     * As an example, let's say you want to write an array of records,
     * the record consisting of an Long field and a Boolean field.
     * Your code would look something like this:
     * <pre>
     *  out.writeArrayStart();
     *  out.setItemCount(list.size());
     *  for (Record r : list) {
     *    out.startItem();
     *    out.writeLong(r.longField);
     *    out.writeBoolean(r.boolField);
     *  }
     *  out.writeArrayEnd();
     *  </pre>
     *
     * @throws BaijiTypeException If this is a stateful writer and an
     *                            array is not expected
     */
    void writeArrayStart() throws IOException;

    /**
     * Call this method before writing a batch of items in an array or a map.
     * Then for each item, call {@link #startItem()} followed by any of the
     * other write methods of {@link Encoder}. The number of calls
     * to {@link #startItem()} must be equal to the count specified
     * in {@link #setItemCount}. Once a batch is completed you
     * can start another batch with {@link #setItemCount}.
     *
     * @param itemCount The number of {@link #startItem()} calls to follow.
     * @throws java.io.IOException
     */
    void setItemCount(long itemCount) throws IOException;

    /**
     * Start a new item of an array or map.
     * See {@link #writeArrayStart} for usage information.
     *
     * @throws BaijiTypeException If called outside of an array or map context
     */
    void startItem() throws IOException;

    /**
     * Call this method to finish writing an array.
     * See {@link #writeArrayStart} for usage information.
     *
     * @throws BaijiTypeException If items written does not match count
     *                            provided to {@link #writeArrayStart}
     * @throws BaijiTypeException If not currently inside an array
     */
    void writeArrayEnd() throws IOException;

    /**
     * Call this to start a new map.  See
     * {@link #writeArrayStart} for details on usage.
     * <p/>
     * As an example of usage, let's say you want to write a map of
     * records, the record consisting of an Long field and a Boolean
     * field.  Your code would look something like this:
     * <pre>
     * out.writeMapStart();
     * out.setItemCount(list.size());
     * for (Map.Entry<String,Record> entry : map.entrySet()) {
     *   out.startItem();
     *   out.writeString(entry.getKey());
     *   out.writeLong(entry.getValue().longField);
     *   out.writeBoolean(entry.getValue().boolField);
     * }
     * out.writeMapEnd();
     * </pre>
     *
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            map is not expected
     */
    void writeMapStart() throws IOException;

    /**
     * Call this method to terminate the inner-most, currently-opened
     * map.  See {@link #writeArrayStart} for more details.
     *
     * @throws BaijiTypeException If items written does not match count
     *                            provided to {@link #writeMapStart}
     * @throws BaijiTypeException If not currently inside a map
     */
    void writeMapEnd() throws IOException;

    /**
     * Call this method to write the tag of a union.
     * <p/>
     * As an example of usage, let's say you want to write a union,
     * whose second branch is a record consisting of an Long field and
     * a Boolean field.  Your code would look something like this:
     * <pre>
     * out.writeUnionIndex(1);
     * out.writeLong(record.longField);
     * out.writeBoolean(record.boolField);
     * </pre>
     *
     * @throws BaijiTypeException If this is a stateful writer and a
     *                            map is not expected
     */
    void writeUnionIndex(int unionIndex) throws IOException;
}

