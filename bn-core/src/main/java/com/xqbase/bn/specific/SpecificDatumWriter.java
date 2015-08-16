package com.xqbase.bn.specific;

import com.xqbase.bn.generic.GenericDatumWriter;
import com.xqbase.bn.io.Encoder;
import com.xqbase.bn.schema.EnumSchema;
import com.xqbase.bn.schema.Schema;

import java.io.IOException;

/**
 * {@link com.xqbase.bn.io.DatumWriter} for generated Java classes.
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
