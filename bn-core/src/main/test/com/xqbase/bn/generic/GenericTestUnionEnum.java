package com.xqbase.bn.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Generic Test Enum.
 *
 * @author Tony He
 */
@RunWith(Parameterized.class)
public class GenericTestUnionEnum extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"\"int\"", 10, 10}
        });
    }

    private final String schema;
    private final Object actual;
    private final Object expected;

    public GenericTestUnionEnum(String schema, Object actual, Object expected) {
        this.schema = schema;
        this.actual = actual;
        this.expected = expected;
    }

    @Test
    public void testUnionEnum() throws IOException {
        testResolution(schema, actual, expected);
    }
}
