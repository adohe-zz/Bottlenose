package com.xqbase.baiji.specific;

import com.xqbase.baiji.generic.GenericDatumWriter;
import com.xqbase.baiji.io.Encoder;
import com.xqbase.baiji.schema.EnumSchema;
import com.xqbase.baiji.schema.Schema;

import java.io.IOException;

/**
 * {@link com.xqbase.baiji.io.DatumWriter} for generated Java classes.
 *
 * @author Tony He
 */
public class SpecificDatumWriter<T> extends GenericDatumWriter<T> {

    public SpecificDatumWriter(Schema schema) {
        super(schema, SpecificData.get());
    }

    public SpecificDatumWriter(Schema schema, SpecificData data) {
        super(schema, data);
    }

    @Override
    protected void writeEnum(EnumSchema enumSchema, Object datum, Encoder out) throws IOException {
        if (!(datum instanceof Enum)) {
            super.writeEnum(enumSchema, datum, out);
        } else {
            out.writeEnum(((Enum) datum).ordinal());
        }
    }
}
