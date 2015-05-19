package com.xqbase.baiji.specific;

import com.xqbase.baiji.generic.GenericDatumReader;
import com.xqbase.baiji.schema.Schema;

/**
 * {@link com.xqbase.baiji.io.DatumReader} for generated Java objects.
 *
 * @author Tony He
 */
public class SpecificDatumReader<T> extends GenericDatumReader<T> {

    public SpecificDatumReader(Schema schema) {
        super(schema);
    }
}
