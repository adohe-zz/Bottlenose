package com.xqbase.baiji;

import com.xqbase.baiji.specific.SpecificRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Serializer with JSON encoding.
 *
 * @author Tony He
 */
public class JSONSerializer implements Serializer {

    @Override
    public <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException {

    }

    @Override
    public <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        return null;
    }
}
