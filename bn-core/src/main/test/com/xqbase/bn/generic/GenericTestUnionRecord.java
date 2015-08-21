package com.xqbase.bn.generic;

import com.xqbase.bn.schema.RecordSchema;
import com.xqbase.bn.schema.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Generic Test Union Record.
 *
 * @author Tony He
 */
@RunWith(Parameterized.class)
public class GenericTestUnionRecord extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"[{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"string\"}]}, \"string\"]",
                        "{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"string\"}]}",
                        new Object[]{"f1", "v1"}}
        });
    }

    private final String unionSchema;
    private final String recordSchema;
    private final Object[] value;

    public GenericTestUnionRecord(String unionSchema, String recordSchema, Object[] value) {
        this.unionSchema = unionSchema;
        this.recordSchema = recordSchema;
        this.value = value;
    }

    @Test
    public void testUnionRecord() throws IOException {
        test(unionSchema, makeRecord(value, (RecordSchema) Schema.parse(recordSchema)));
    }
}
