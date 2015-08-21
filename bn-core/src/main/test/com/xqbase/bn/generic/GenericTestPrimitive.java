package com.xqbase.bn.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Generic Test Primitive.
 *
 * @author Tony He
 */
@RunWith(Parameterized.class)
public class GenericTestPrimitive extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{"{\"type\": \"boolean\"}", true},
                new Object[]{"{\"type\": \"boolean\"}", false},
                // Union
                new Object[]{"[\"boolean\", \"null\"]", null},
                new Object[]{"[\"boolean\", \"null\"]", true},
                new Object[]{"[\"int\", \"long\"]", 100},
                new Object[]{"[\"int\", \"long\"]", 100L},
                new Object[]{"[\"float\", \"double\"]", 100.75},
                new Object[]{"[\"float\", \"double\"]", 23.67f},
                new Object[]{"[{\"type\": \"array\", \"items\": \"float\"}, \"double\"]", new Float[]{23.67f, 22.78f}},
                new Object[]{"[{\"type\": \"array\", \"items\": \"float\"}, \"double\"]", 100.89},
                new Object[]{"[{\"type\": \"array\", \"items\": \"string\"}, \"string\"]", "a"},
                new Object[]{"[{\"type\": \"array\", \"items\": \"string\"}, \"string\"]", new String[]{"a", "b"}},
                new Object[]{"[{\"type\": \"array\", \"items\": \"bytes\"}, \"bytes\"]", new byte[]{1, 2, 3}},
                new Object[]{"[{\"type\": \"array\", \"items\": \"bytes\"}, \"bytes\"]", new Object[]{new byte[]{1, 2}, new byte[]{3, 4}}},
                new Object[]{"[{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}, \"string\"]", "h1"});
    }

    private final String schema;
    private final Object value;

    public GenericTestPrimitive(String schema, Object value) {
        this.schema = schema;
        this.value = value;
    }

    @Test
    public void testPrimitive() throws IOException {
        test(this.schema, this.value);
    }
}
