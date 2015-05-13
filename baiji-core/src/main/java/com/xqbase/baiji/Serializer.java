package com.xqbase.baiji;

import com.xqbase.baiji.specific.SpecificRecord;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The Serializer Interface.
 *
 * @author Tony He
 */
public interface Serializer {

    /**
     * Serialize the given object into the stream.
     */
    <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException;

    /**
     * Deserialize an object with the given type from the stream
     */
    <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException;
}
