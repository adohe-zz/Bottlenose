package com.xqbase.bn.generic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

/**
 * Generic Test Resolution Enum.
 *
 * @author Tony He
 */
@RunWith(Parameterized.class)
public class GenericTestResolutionEnum extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\":\"enum\", \"symbols\":[\"a\", \"b\"], \"name\":\"e\"}", "a"}
        });
    }

    private final String schema;
    private final String symbol;

    public GenericTestResolutionEnum(String schema, String symbol) {
        this.schema = schema;
        this.symbol = symbol;
    }

    @Test
    public void testResolutionEnum() throws IOException {
        testResolution(schema, makeEnum(schema, "a"), makeEnum(schema, "a"));
    }
}
