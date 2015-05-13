package com.xqbase.baiji.specific;

import com.xqbase.baiji.generic.GenericDatumWriter;
import com.xqbase.baiji.schema.Schema;

/**
 * {@link com.xqbase.baiji.io.DatumWriter} for generated Java classes.
 *
 * @author Tony He
 */
public class SpecificDatumWriter<T> extends GenericDatumWriter<T> {
    @Override
    public Schema getSchema() {
        return null;
    }
}
