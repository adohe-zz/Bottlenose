package com.xqbase.bn.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class TestSchemaFullName {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                new Object[]{"a", "o.a.h", "o.a.h.a"}
        });
    }

    private final String name;
    private final String namespace;
    private final String fullName;

    public TestSchemaFullName(String name, String namespace, String fullName) {
        this.name = name;
        this.namespace = namespace;
        this.fullName = fullName;
    }

    @Test
    public void testFullName() {
        SchemaName schemaName = new SchemaName(name, namespace);
        Assert.assertEquals(fullName, schemaName.getFullName());
    }
}
