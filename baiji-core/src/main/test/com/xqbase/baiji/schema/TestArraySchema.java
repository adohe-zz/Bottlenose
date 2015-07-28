package com.xqbase.baiji.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestArraySchema extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"{\"type\": \"array\", \"items\": \"long\"}", "long"}
        });
    }

    private final String schema;
    private final String itemType;

    public TestArraySchema(String schema, String itemType) {
        this.schema = schema;
        this.itemType = itemType;
    }

    @Test
    public void testArray() {
        Schema sc = Schema.parse(schema);
        Assert.assertTrue(sc instanceof ArraySchema);
        Assert.assertEquals(SchemaType.ARRAY, sc.getType());

        ArraySchema ars = (ArraySchema) sc;
        Assert.assertEquals(itemType, ars.getItemSchema().getName());

        testEquality(schema, sc);
        testToString(sc);
    }
}
