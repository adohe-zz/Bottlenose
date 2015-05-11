package com.xqbase.baiji.generic;

import com.xqbase.baiji.io.DatumWriter;
import com.xqbase.baiji.io.Encoder;
import com.xqbase.baiji.schema.Schema;

import java.io.IOException;

/**
 * {@link com.xqbase.baiji.io.DatumWriter} for generic Java objects.
 *
 * @author Tony He
 */
public class GenericDatumWriter<D> implements DatumWriter<D> {

    @Override
    public void setSchema(Schema schema) {

    }

    @Override
    public void write(D datum, Encoder out) throws IOException {

    }
}
