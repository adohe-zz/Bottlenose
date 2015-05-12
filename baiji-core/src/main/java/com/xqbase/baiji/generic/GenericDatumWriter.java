package com.xqbase.baiji.generic;

import com.xqbase.baiji.io.DatumWriter;
import com.xqbase.baiji.io.Encoder;
import com.xqbase.baiji.schema.Field;
import com.xqbase.baiji.schema.RecordSchema;
import com.xqbase.baiji.schema.Schema;

import java.io.IOException;

/**
 * {@link com.xqbase.baiji.io.DatumWriter} for generic Java objects.
 *
 * @author Tony He
 */
public abstract class GenericDatumWriter<D> implements DatumWriter<D> {

    private Schema schema;

    public GenericDatumWriter() {

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

    protected void write(Schema schema, Object datum, Encoder out) {

    }

    protected void writeRecord(RecordSchema recordSchema, Object datum, Encoder out) {
        for (Field field : recordSchema.getFields()) {

        }
    }

    protected void writeField(Field field, Object datum, Encoder out) {

    }
}
