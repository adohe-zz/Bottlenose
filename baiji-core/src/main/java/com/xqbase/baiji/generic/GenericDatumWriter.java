package com.xqbase.baiji.generic;

import com.xqbase.baiji.io.Encoder;

import java.io.IOException;

/**
 * {@link com.xqbase.baiji.io.DatumWriter} for generic Java objects.
 *
 * @author Tony He
 */
public class GenericDatumWriter<D> extends PresolvingDatumWriter<D> {

    @Override
    public void write(D datum, Encoder out) throws IOException {

    }
}
