package com.xqbase.bn.generic;

import com.xqbase.bn.schema.EnumSchema;
import com.xqbase.bn.schema.RecordSchema;
import com.xqbase.bn.schema.Schema;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Generic Test Base class.
 *
 * @author Tony He
 */
public abstract class GenericTestBase {

    protected static <T> void test(String schema, T value) throws IOException {
        Schema s = Schema.parse(schema);
        byte[] data = serialize(s, value);
        T output = deserialize(data, s);
        checkEquals(value, output);
    }

    protected <T, S> void testResolution(String schema, T actual, S expected) throws IOException {
        Schema s = Schema.parse(schema);
        byte[] data = serialize(s, actual);
        S output = deserialize(data, s);
        checkEquals(expected, output);
    }

    protected static GenericRecord makeRecord(Object[] kv, RecordSchema s) {
        GenericRecord input = new GenericRecord(s);
        for (int i = 0; i < kv.length; i += 2) {
            String fieldName = (String) kv[i];
            Object fieldValue = kv[i + 1];
            Schema inner = s.getField(fieldName).getSchema();
            if (inner instanceof EnumSchema) {
                GenericEnum ge = new GenericEnum((EnumSchema) inner, (String) fieldValue);
                fieldValue = ge;
            }
            input.add(fieldName, fieldValue);
        }
        return input;
    }

    protected static Map<String, Object> makeMap(Object[] vv) {
        Map<String, Object> d = new HashMap<String, Object>();
        for (int j = 0; j < vv.length; j += 2) {
            d.put((String) vv[j], vv[j + 1]);
        }
        return d;
    }

    protected static Object makeEnum(String enumSchema, String value) {
        return new GenericEnum((EnumSchema) Schema.parse(enumSchema), value);
    }

    protected static <S> S deserialize(byte[] data, Schema s) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        GenericDatumReader<S> r = new GenericDatumReader<S>(s);
        Decoder d = new BinaryDecoder(is);
        List<S> items = new ArrayList<S>();
        // validate reading twice to make sure there isn't some state that isn't reset between reads.
        items.add(read(r, d));
        items.add(read(r, d));
        assertEquals(0, is.available()); // Ensure we have read everything.
        checkAlternateDeserializers(items, data, s);
        return items.get(0);
    }

    protected static <S> S read(DatumReader<S> reader, Decoder d) throws IOException {
        return reader.read(null, d);
    }

    protected static <S> void checkAlternateDeserializers(List<S> expectations, byte[] data,
                                                          Schema s) throws IOException {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        GenericDatumReader<S> r = new GenericDatumReader<S>(s);
        Decoder d = new BinaryDecoder(is);
        for (Object expected : expectations) {
            S read = read(r, d);
            checkEquals(expected, read);
        }
        assertEquals(0, is.available()); // Ensure we have read everything.
    }

    protected static <T> byte[] serialize(Schema schema, T actual) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Encoder e = new BinaryEncoder(os);
        GenericDatumWriter<T> w = new GenericDatumWriter<T>(schema);
        // write twice so we can validate reading twice
        w.write(actual, e);
        w.write(actual, e);
        byte[] data = os.toByteArray();
        checkAlternateSerializers(data, actual, schema);
        return data;
    }

    protected static <T> void checkAlternateSerializers(byte[] expected, T value, Schema schema)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Encoder e = new BinaryEncoder(os);
        GenericDatumWriter<T> w = new GenericDatumWriter<T>(schema);
        w.write(value, e);
        w.write(value, e);
        byte[] output = os.toByteArray();
        assertArrayEquals(expected, output);
    }

    private static void checkEquals(Object expected, Object actual) {
        if (expected == actual) {
            return;
        }
        if (expected instanceof Object[]) {
            Object[] expectedArray = (Object[]) expected;
            Object[] actualArray = (Object[]) actual;
            Assert.assertEquals(expectedArray.length, actualArray.length);
            for (int i = 0; i < expectedArray.length; ++i) {
                checkEquals(expectedArray[i], actualArray[i]);
            }
        } else if (expected instanceof byte[]) {
            Assert.assertArrayEquals((byte[]) expected, (byte[]) actual);
        } else {
            Assert.assertEquals(expected, actual);
        }
    }
}
