package com.xqbase.baiji.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestUnionSchema extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"[\"string\", \"null\", \"long\"]", new String[]{"string", "null", "long"}}
        });
    }

    private final String schema;
    private final String[] types;

    public TestUnionSchema(String schema, String[] types) {
        this.schema = schema;
        this.types = types;
    }

    @Test
    public void testUnion() {
        Schema sc = Schema.parse(schema);
        Assert.assertTrue(sc instanceof UnionSchema);
        Assert.assertEquals(SchemaType.UNION, sc.getType());

        UnionSchema us = (UnionSchema) sc;
        Assert.assertEquals(types.length, us.size());

        for (int i = 0; i < us.size(); i++) {
            Assert.assertEquals(types[i], us.get(i).getName());
        }

        testEquality(schema, sc);
        testToString(sc);
    }
}
