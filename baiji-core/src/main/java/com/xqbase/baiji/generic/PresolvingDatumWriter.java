package com.xqbase.baiji.generic;

import com.xqbase.baiji.exceptions.BaijiRuntimeException;
import com.xqbase.baiji.io.DatumWriter;
import com.xqbase.baiji.io.Encoder;
import com.xqbase.baiji.schema.Schema;

import java.io.IOException;
import java.util.Map;

/**
 * A general purpose writer of data from Baiji streams. This writer analyzes the writer schema
 * when constructed so that writes can be more efficient. Once constructed, a writer can be
 * reused or shared among threads to avoid incurring more resolution costs.
 *
 * @author Tony He
 */
public class PresolvingDatumWriter<T> implements DatumWriter<T> {

    private final Schema schema;
    private final ItemWriter itemWriter;

    protected PresolvingDatumWriter(Schema schema) {
        this.schema = schema;
        this.itemWriter = null;
    }

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
    public void write(T datum, Encoder out) throws IOException {

    }

    protected static interface ItemWriter {
        void write(Object value, Encoder encoder) throws IOException;
    }

    protected static BaijiRuntimeException typeMismatch(Object obj, String schemaType, String type) {
        return new BaijiRuntimeException(type + " required to write against " + schemaType + " schema but found " +
                (null == obj ? "null" : obj.getClass().toString()));
    }

    protected static interface ArrayAccess {
        /**
         * Checks if the given object is an array. If it is a valid array, this function returns normally. Otherwise,
         * it throws an exception. The default implementation checks if the value is an array.
         *
         */
        void ensureArrayObject(Object value);

        /**
         * Returns the length of an array. The default implementation requires the object
         * to be an array of objects and returns its length. The default implementation
         * guarantees that ensureArrayObject() has been called on the value before this
         * function is called.
         *
         * @param value The object whose array length is required
         * @return The array length of the given object
         */
        long getArrayLength(Object value);


        /**
         * Returns the element at the given index from the given array object. The default implementation
         * requires that the value is an object array and returns the element in that array. The defaul implementation
         * guarantees that ensureArrayObject() has been called on the value before this
         * function is called.
         *
         * @param array       The array object
         * @param valueWriter
         * @param encoder
         * @throws IOException
         */
        void writeArrayValues(Object array, ItemWriter valueWriter, Encoder encoder) throws IOException;
    }

    protected static interface MapAccess {
        /**
         * Checks if the given object is a map. If it is a valid map, this function returns normally. Otherwise,
         * it throws an exception. The default implementation checks if the value is an IDictionary{string, object}.
         *
         * @param value
         */
        void ensureMapObject(Object value);

        /**
         * Returns the size of the map object. The default implementation guarantees that EnsureMapObject has been
         * successfully called with the given value. The default implementation requires the value
         * to be a {@link java.util.Map} and returns the number of elements in it.
         *
         * @param value The map object whose size is desired
         * @return The size of the given map object
         */
        long getMapSize(Object value);


        /**
         * Returns the contents of the given map object. The default implementation guarantees that EnsureMapObject
         * has been called with the given value. The default implementation of this method requires that
         * the value is a {@link java.util.Map} and returns its contents.
         *
         * @param map         The map object whose size is desired
         * @param valueWriter
         * @param encoder
         * @throws IOException
         */
        void writeMapValues(Object map, ItemWriter valueWriter, Encoder encoder) throws IOException;
    }

    protected static class DefaultMapAccess implements MapAccess {

        public DefaultMapAccess() {
        }

        public void ensureMapObject(Object value) {
            if (!(value instanceof Map)) {
                throw typeMismatch(value, "map", "Map");
            }
        }

        public long getMapSize(Object value) {
            Map map = (Map) value;
            long size = 0L;
            for (Map.Entry entry : ((Map<?, ?>) map).entrySet()) {
                Object k = entry.getKey();
                Object v = entry.getValue();
                if (k != null && v != null)
                    size++;
            }
            return size;
        }

        public void writeMapValues(Object map, ItemWriter valueWriter, Encoder encoder) throws IOException {
            for (Map.Entry entry : ((Map<?, ?>) map).entrySet()) {
                Object key = entry.getKey();
                Object value = entry.getValue();
                if (key != null && value != null) {
                    encoder.startItem();
                    encoder.writeString(entry.getKey().toString());
                    valueWriter.write(entry.getValue(), encoder);
                }
            }
        }
    }
}
