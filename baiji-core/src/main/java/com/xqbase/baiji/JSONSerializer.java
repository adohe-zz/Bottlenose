package com.xqbase.baiji;

import com.xqbase.baiji.io.*;
import com.xqbase.baiji.specific.SpecificDatumReader;
import com.xqbase.baiji.specific.SpecificDatumWriter;
import com.xqbase.baiji.specific.SpecificRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Serializer with JSON encoding.
 *
 * @author Tony He
 */
public class JSONSerializer implements Serializer {

    private static final ConcurrentHashMap<Class<?>, DatumWriter> writerCache = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<Class<?>, DatumReader> readerCache = new ConcurrentHashMap<>();

    @Override
    public <T extends SpecificRecord> void serialize(T obj, OutputStream stream) throws IOException {
        DatumWriter<T> writer = getWriter(obj);
        writer.write(obj, new JsonEncoder(obj.getSchema(), stream));
    }

    @Override
    public <T extends SpecificRecord> T deserialize(Class<T> objClass, InputStream stream) throws IOException {
        DatumReader<T> reader = getReader(objClass);
        return reader.read(null, new DirectBinaryDecoder(stream));
    }

    @SuppressWarnings("unchecked")
    private <T extends SpecificRecord> DatumWriter<T> getWriter(T obj) {
        Class<?> clazz = obj.getClass();
        DatumWriter<T> writer = writerCache.get(clazz);
        if (null == writer) {
            writer = new SpecificDatumWriter(obj.getSchema());
            DatumWriter<T> existedWriter = writerCache.putIfAbsent(clazz, writer);
            if (existedWriter != null) {
                writer = existedWriter;
            }
        }
        return writer;
    }

    @SuppressWarnings("unchecked")
    private <T extends SpecificRecord> DatumReader<T> getReader(Class<T> clazz) {
        DatumReader<T> reader = readerCache.get(clazz);
        if (null == reader) {
            SpecificRecord record;
            try {
                Constructor<T> ctor = clazz.getDeclaredConstructor();
                ctor.setAccessible(true);
                record = ctor.newInstance();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            reader = new SpecificDatumReader(record.getSchema());
            DatumReader<T> existedReader = readerCache.putIfAbsent(clazz, reader);
            if (existedReader != null) {
                reader = existedReader;
            }
        }
        return reader;
    }
}
