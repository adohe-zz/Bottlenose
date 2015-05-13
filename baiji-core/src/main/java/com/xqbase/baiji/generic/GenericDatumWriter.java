package com.xqbase.baiji.generic;

import com.xqbase.baiji.exceptions.BaijiTypeException;
import com.xqbase.baiji.io.DatumWriter;
import com.xqbase.baiji.io.Encoder;
import com.xqbase.baiji.schema.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * {@link com.xqbase.baiji.io.DatumWriter} for generic Java objects.
 *
 * @author Tony He
 */
public abstract class GenericDatumWriter<D> implements DatumWriter<D> {

    private final GenericData data;
    private Schema schema;

    public GenericDatumWriter() {
        this(GenericData.get());
    }

    protected GenericDatumWriter(GenericData data) {
        this.data = data;
    }

    public GenericDatumWriter(Schema schema) {
        this();
        setSchema(schema);
    }

    public GenericDatumWriter(Schema schema, GenericData data) {
        this.data = data;
        setSchema(schema);
    }

    @Override
    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    /**
     * Helper method for adding a message to an NPE.
     */
    private NullPointerException npe(NullPointerException e, String s) {
        NullPointerException result = new NullPointerException(e.getMessage() + s);
        result.initCause(e.getCause() == null ? e : e.getCause());
        return result;
    }

    @Override
    public void write(D datum, Encoder out) throws IOException {
        write(schema, datum, out);
    }

    protected void write(Schema schema, Object datum, Encoder out)
                throws IOException {
        try {
            switch (schema.getType()) {
                case RECORD:
                    writeRecord((RecordSchema) schema, datum, out);
                    break;
                case ENUM:
                    writeEnum((EnumSchema) schema, datum, out);
                    break;
                case ARRAY:
                    writeArray((ArraySchema) schema, datum, out);
                    break;
                case MAP:
                    writeMap((MapSchema) schema, datum, out);
                    break;
                case STRING:
                    writeString(schema, datum, out);
                    break;
                case BYTES:
                    writeBytes(schema, datum, out);
                    break;
                case INT:
                    out.writeInt(((Number) datum).intValue());
                    break;
                case LONG:
                    out.writeLong((Long) datum);
                    break;
                case FLOAT:
                    out.writeFloat((Float) datum);
                    break;
                case DOUBLE:
                    out.writeDouble((Double) datum);
                    break;
                case DATETIME:
                    out.writeDatetime((Calendar) datum);
                    break;
                case BOOLEAN:
                    out.writeBoolean((Boolean) datum);
                    break;
                case NULL:
                    out.writeNull();
                    break;
                default:
                    error(schema, datum);
            }
        } catch (NullPointerException e) {
            throw npe(e, " of " + schema.getName());
        }
    }

    /**
     * Called to write a record. May be overridden for alternate record representations.
     */
    protected void writeRecord(RecordSchema recordSchema, Object datum, Encoder out) throws IOException {
        for (Field field : recordSchema.getFields()) {
            writeField(field, datum, out);
        }
    }

    /**
     * Called to write a single field of a record. May be overridden for more
     * efficient or alternate implementations.
     */
    protected void writeField(Field field, Object datum, Encoder out) throws IOException {
        Object value = data.getField(datum, field.getName(), field.getPos());
        try {
            write(field.getSchema(), value, out);
        } catch (NullPointerException e) {
            throw npe(e, " in field " + field.getName());
        }
    }

    /**
     * Called to write an enum value. May be overridden for alternate enum representations.
     */
    protected void writeEnum(EnumSchema enumSchema, Object datum, Encoder out) throws IOException {
        out.writeEnum(enumSchema.getEnumOrdinal(datum.toString()));
    }

    /**
     * Called to write an array. May be overridden for alternate array representations.
     */
    protected void writeArray(ArraySchema arraySchema, Object datum, Encoder out) throws IOException {
        Schema itemSchema = arraySchema.getItemSchema();
        long size = getArraySize(datum);
        long actualSize = 0;
        out.writeArrayStart();
        out.setItemCount(size);
        for (Iterator<?> iterator = getArrayElements(datum); iterator.hasNext();) {
            out.startItem();
            write(itemSchema, iterator.next(), out);
            actualSize ++;
        }
        out.writeArrayEnd();

        if (actualSize != size) {
            throw new ConcurrentModificationException("Size of array written was " +
                    size + " , but number of elements written was " + actualSize);
        }
    }

    /**
     * Called by the default implementation of {@link #writeArray} to get
     * size of an array.
     */
    protected long getArraySize(Object array) {
        return ((Collection) array).size();
    }

    /**
     * Called by the default implementation of {@link #writeArray} to get
     * array elements.
     */
    @SuppressWarnings("unchecked")
    protected Iterator<?> getArrayElements(Object array) {
        return ((Collection) array).iterator();
    }

    /**
     * Called to write a map. May be overridden by alternate map representations.
     */
    protected void writeMap(MapSchema mapSchema, Object datum, Encoder out) throws IOException {
        Schema valueSchema = mapSchema.getValueSchema();
        long size = getMapSize(datum);
        long actualSize = 0;
        out.setItemCount(size);
        out.writeMapStart();
        for (Map.Entry<String, Object> entry : getMapEntry(datum)) {
            out.startItem();
            out.writeString(entry.getKey());
            write(valueSchema, entry.getValue(), out);
            actualSize ++;
        }
        out.writeMapEnd();

        if (actualSize != size) {
            throw new ConcurrentModificationException("Size of map written was " +
                    size + " , but number of elements written was " + actualSize);
        }
    }

    /**
     * Called by the default implementation of {@link #writeMap} to get size
     * of a map.
     */
    @SuppressWarnings("unchecked")
    protected long getMapSize(Object map) {
        return ((Map) map).size();
    }

    /**
     * Called by the default implementation of {@link #writeMap} to get
     * entry set of a map.
     */
    @SuppressWarnings("unchecked")
    protected Iterable<Map.Entry<String, Object>> getMapEntry(Object map) {
        return ((Map) map).entrySet();
    }

    /**
     * Called to write a string. May be overridden by alternate string representation.
     */
    protected void writeString(Schema schema, Object datum, Encoder out) throws IOException {
        writeString(datum, out);
    }

    /**
     * Called to write a string. May be overridden by alternate string representation.
     */
    protected void writeString(Object datum, Encoder out) throws IOException {
        out.writeString((CharSequence) datum);
    }

    /**
     * Called to write a bytes. May be overridden by alternate bytes representation.
     */
    protected void writeBytes(Schema schema, Object datum, Encoder out) throws IOException {
        out.writeBytes((ByteBuffer) datum);
    }

    private void error(Schema schema, Object datum) {
        throw new BaijiTypeException("Not a" + schema + ": " + datum);
    }
}
