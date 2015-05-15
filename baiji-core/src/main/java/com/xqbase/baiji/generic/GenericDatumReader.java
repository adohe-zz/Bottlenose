package com.xqbase.baiji.generic;

import com.xqbase.baiji.io.DatumReader;
import com.xqbase.baiji.io.Decoder;

import java.io.IOException;

/**
 * {@link com.xqbase.baiji.io.DatumReader} for generic Java objects.
 *
 * @author Tony He
 */
public abstract class GenericDatumReader<D> implements DatumReader<D> {

    public GenericDatumReader() {

    }

    @Override
    public D read(D reuse, Decoder in) throws IOException {
        return null;
    }
}
