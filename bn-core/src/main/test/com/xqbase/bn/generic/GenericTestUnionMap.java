package com.xqbase.bn.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Generic Test Union Map.
 *
 * @author Tony He
 */
@RunWith(Parameterized.class)
public class GenericTestUnionMap extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"[{\"type\": \"map\", \"values\": \"int\"}, \"string\"]",
                        "{\"type\": \"map\", \"values\": \"int\"}", new Object[]{"a", 1, "b", 2}}
        });
    }

    private final String unionSchema;
    private final String mapSchema;
    private final Object[] value;

    public GenericTestUnionMap(String unionSchema, String mapSchema, Object[] value) {
        this.unionSchema = unionSchema;
        this.mapSchema = mapSchema;
        this.value = value;
    }

    @Test
    public void testUnionMap() throws IOException {
        test(unionSchema, makeMap(value));
    }
}
