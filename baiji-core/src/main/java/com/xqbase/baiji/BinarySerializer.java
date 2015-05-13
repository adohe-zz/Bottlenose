package com.xqbase.baiji;

import com.xqbase.baiji.io.DatumWriter;
import com.xqbase.baiji.io.DirectBinaryEncoder;
import com.xqbase.baiji.schema.Schema;
import com.xqbase.baiji.specific.SpecificDatumWriter;
import com.xqbase.baiji.specific.SpecificRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serializer with binary encoding.
 *
 * @author Tony He
 */
public class BinarySerializer implements Serializer {

    private static final ConcurrentHashMap<Class<?>, DatumWriter> writerCache = new ConcurrentHashMap<>();

    @Override
    public <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException {
        DatumWriter<T> writer = getWriter(obj);
        writer.write(obj, new DirectBinaryEncoder(stream));
    }

    @Override
    public <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        return null;
    }

    @SuppressWarnings("unchecked")
    private <T extends SpecificRecord> DatumWriter<T> getWriter(T obj) {
        Class<?> clazz = obj.getClass();
        DatumWriter writer = writerCache.get(clazz);
        if (null == writer) {
            Schema schema = obj.getSchema();
            DatumWriter existedWriter = writerCache.putIfAbsent(clazz, new SpecificDatumWriter(schema));
            if (existedWriter != null) {
                writer = existedWriter;
            }
        }

        return writer;
    }
}
