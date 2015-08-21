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
 * Generic Test Resolution Record.
 *
 * @author Tony He
 */
@RunWith(Parameterized.class)
public class GenericTestResolutionRecord extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\":\"record\",\"name\":\"r\",\"fields\":" +
                        "[{\"name\":\"f1\",\"type\":\"boolean\"},{\"name\":\"f2\",\"type\":\"int\"}]}",
                        new Object[]{"f1", true, "f2", 100},
                        new Object[]{"f1", true, "f2", 100}}
        });
    }

    private final String schema;
    private final Object[] actual;
    private final Object[] expected;

    public GenericTestResolutionRecord(String schema, Object[] actual, Object[] expected) {
        this.schema = schema;
        this.actual = actual;
        this.expected = expected;
    }

    @Test
    public void testResolutionRecord() throws IOException {
        RecordSchema s = (RecordSchema) Schema.parse(schema);
        testResolution(schema, makeRecord(actual, s), makeRecord(expected, s));
    }
}
