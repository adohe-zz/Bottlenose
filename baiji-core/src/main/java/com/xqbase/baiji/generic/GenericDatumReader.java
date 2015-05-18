package com.xqbase.baiji.generic;

import com.xqbase.baiji.io.DatumReader;
import com.xqbase.baiji.io.Decoder;
import com.xqbase.baiji.schema.Schema;

import java.io.IOException;

/**
 * {@link com.xqbase.baiji.io.DatumReader} for generic java classes.
 *
 * @author Tony He
 */
public abstract class GenericDatumReader<T> implements DatumReader<T> {

    private final GenericData data;
    private Schema schema;

    public GenericDatumReader(Schema schema) {
        this(schema, GenericData.get());
    }

    public GenericDatumReader(Schema schema, GenericData data) {
        this(data);
        this.schema = schema;
    }

    protected GenericDatumReader(GenericData data) {
        this.data = data;
    }

    @Override
    public T read(T reuse, Decoder in) throws IOException {
        return null;
    }

    protected Object read(Object reuse, Schema schema, Decoder in) {
        return null;
    }
}
