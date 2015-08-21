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
 * Generic Test Record.
 *
 * @author Tony He
 */
@RunWith(Parameterized.class)
public class GenericTestRecord extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"null\"}]}", new Object[]{"f1", null}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"boolean\"}]}", new Object[]{"f1", true}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"boolean\"}]}", new Object[]{"f1", false}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"int\"}]}", new Object[]{"f1", 101}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"long\"}]}", new Object[]{"f1", 101L}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"float\"}]}", new Object[]{"f1", 101.78f}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"double\"}]}", new Object[]{"f1", 101.78}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"string\"}]}", new Object[]{"f1", "A"}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":\"bytes\"}]}", new Object[]{"f1", new byte[]{0, 1}}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\": [{\"name\":\"f1\", \"type\":{\"type\": \"enum\", \"name\": \"e\", \"symbols\":[\"s1\",\"s2\"]}}]}", new Object[]{"f1", "s2"}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\": [{\"name\":\"f1\", \"type\":{\"type\": \"array\", \"items\": \"int\"}}]}", new Object[]{"f1", new Object[]{0, 1, 101}}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\": [{\"name\":\"f1\", \"type\":{\"type\": \"array\", \"items\": \"int\"}}]}", new Object[]{"f1", new Integer[]{0, 1, 101}}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\": [{\"name\":\"f1\", \"type\":[\"int\", \"long\"]}]}", new Object[]{"f1", 100}},
                new Object[]{"{\"type\":\"record\", \"name\":\"n\", \"fields\":[{\"name\":\"f1\", \"type\":[\"int\", \"long\"]}]}", new Object[]{"f1", 100L}});
    }

    private final String schema;
    private final Object[] kv;

    public GenericTestRecord(String schema, Object[] kv) {
        this.schema = schema;
        this.kv = kv;
    }

    @Test
    public void testRecord() throws IOException {
        test(schema, makeRecord(kv, (RecordSchema) Schema.parse(schema)));
    }
}
