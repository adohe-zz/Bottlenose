package com.xqbase.baiji.generic;

import com.xqbase.baiji.io.DatumWriter;
import com.xqbase.baiji.io.Encoder;
import com.xqbase.baiji.schema.Schema;

import java.io.IOException;

/**
 * A general purpose writer of data from Baiji streams. This writer analyzes the writer schema
 * when constructed so that writes can be more efficient. Once constructed, a writer can be
 * reused or shared among threads to avoid incurring more resolution costs.
 *
 * @author Tony He
 */
public class PresolvingDatumWriter<T> implements DatumWriter<T> {

    @Override
    public Schema getSchema() {
        return null;
    }

    @Override
    public void write(T datum, Encoder out) throws IOException {

    }
}
