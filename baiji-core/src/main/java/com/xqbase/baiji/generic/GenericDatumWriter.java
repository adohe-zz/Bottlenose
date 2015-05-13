package com.xqbase.baiji.generic;

import com.xqbase.baiji.io.DatumWriter;
import com.xqbase.baiji.io.Encoder;
import com.xqbase.baiji.schema.*;

import java.io.IOException;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

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

            }
        } catch (NullPointerException e) {

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
}
