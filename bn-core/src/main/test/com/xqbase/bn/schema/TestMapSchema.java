package com.xqbase.bn.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestMapSchema extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\": \"map\", \"values\": \"long\"}", "long"}
        });
    }

    private final String schema;
    private final String valueType;

    public TestMapSchema(String schema, String valueType) {
        this.schema = schema;
        this.valueType = valueType;
    }

    @Test
    public void testMap() {
        Schema sc = Schema.parse(schema);
        Assert.assertTrue(sc instanceof MapSchema);
        Assert.assertEquals(SchemaType.MAP, sc.getType());

        MapSchema ms = (MapSchema) sc;
        Assert.assertEquals(valueType, ms.getValueSchema().getName());

        testEquality(schema, sc);
        testToString(sc);
    }
}
