package com.xqbase.bn.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestRecordSchema extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                "\"fields\":[{\"name\":\"f1\",\"type\":\"long\"}," +
                "{\"name\":\"f2\",\"type\": \"int\"}]}",
                new String[]{"f1", "long", "100", "f2", "int", "10"}},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"f1\",\"type\":\"long\", \"default\": \"100\"}," +
                        "{\"name\":\"f2\",\"type\": \"int\"}]}",
                        new String[]{"f1", "long", "100", "f2", "int", "10"}},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"value\",\"type\":\"long\", \"default\": \"100\"}," +
                        "{\"name\":\"next\",\"type\":[\"LongList\",\"null\"]}]}",
                        new String[]{"value", "long", "100", "next", "union", null}});
    }

    private final String schema;
    private final String[] fields;

    public TestRecordSchema(String schema, String[] fields) {
        this.schema = schema;
        this.fields = fields;
    }

    @Test
    public void testRecord() {
        Schema sc = Schema.parse(schema);
        Assert.assertEquals(SchemaType.RECORD, sc.getType());
        Assert.assertTrue(sc instanceof RecordSchema);
        RecordSchema record = (RecordSchema) sc;
        Assert.assertEquals(fields.length / 3, record.getFieldsSize());
        for (int i = 0; i < fields.length; i += 3) {
            Field f = record.getField(fields[i]);
            Assert.assertEquals(fields[i + 1], f.getSchema().getName());
        }

        testEquality(schema, sc);
        testToString(sc);
    }
}
