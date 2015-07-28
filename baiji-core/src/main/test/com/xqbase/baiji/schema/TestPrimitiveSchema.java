package com.xqbase.baiji.schema;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestPrimitiveSchema extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{"null", SchemaType.NULL},
                new Object[]{"boolean", SchemaType.BOOLEAN},
                new Object[]{"int", SchemaType.INT},
                new Object[]{"long", SchemaType.LONG},
                new Object[]{"float", SchemaType.FLOAT},
                new Object[]{"double", SchemaType.DOUBLE},
                new Object[]{"bytes", SchemaType.BYTES},
                new Object[]{"string", SchemaType.STRING},
                new Object[]{"{ \"type\": \"null\" }", SchemaType.NULL},
                new Object[]{"{ \"type\": \"boolean\" }", SchemaType.BOOLEAN},
                new Object[]{"{ \"type\": \"int\" }", SchemaType.INT},
                new Object[]{"{ \"type\": \"long\" }", SchemaType.LONG},
                new Object[]{"{ \"type\": \"float\" }", SchemaType.FLOAT},
                new Object[]{"{ \"type\": \"double\" }", SchemaType.DOUBLE},
                new Object[]{"{ \"type\": \"bytes\" }", SchemaType.BYTES},
                new Object[]{"{ \"type\": \"string\" }", SchemaType.STRING});
    }

    private final String schema;
    private final SchemaType type;

    public TestPrimitiveSchema(String schema, SchemaType type) {
        this.schema = schema;
        this.type = type;
    }

    @Test
    public void testPrimitive() {
        Schema sc = Schema.parse(schema);
        Assert.assertTrue(sc instanceof PrimitiveSchema);
        Assert.assertEquals(type, sc.getType());

        testEquality(schema, sc);
        System.out.println("schema: " + schema);
        testToString(sc);
    }
}
