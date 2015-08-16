package com.xqbase.bn.specific;

import com.xqbase.bn.generic.GenericDatumReader;
import com.xqbase.bn.schema.Schema;

/**
 * {@link com.xqbase.bn.io.DatumReader} for generated Java objects.
 *
 * @author Tony He
 */
public class SpecificDatumReader<T> extends GenericDatumReader<T> {

    public SpecificDatumReader(Schema schema) {
        super(schema);
    }
}
