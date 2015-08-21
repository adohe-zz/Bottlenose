package com.xqbase.bn.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Generic Test Map.
 *
 * @author Tony He
 */
@RunWith(Parameterized.class)
public class GenericTestMap extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\": \"map\", \"values\": \"string\"}",
                        new Object[]{"a", "0", "b", "1", "c", "101"}}
        });
    }

    private final String schema;
    private final Object[] values;

    public GenericTestMap(String schema, Object[] values) {
        this.schema = schema;
        this.values = values;
    }

    @Test
    public void testMap() throws IOException {
        test(schema, makeMap(values));
    }
}
