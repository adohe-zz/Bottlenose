package com.xqbase.baiji.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaAliasTest {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"namespace\":\"com\", \"aliases\":[\"c\",\"foo.y\"], \"fields\": [{\"name\":\"f1\",\"type\":\"long\", \"extraprop\":\"important\", \"id\":\"1029\", \"aliases\":[\"a\",\"b\",\"c\"] }, {\"name\":\"f2\",\"type\": \"int\"}]}",
                true},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"aliases\":[\"Alias1\"], \"fields\":[{\"name\":\"f1\",\"type\":\"long\", \"order\":\"junk\" }, {\"name\":\"f2\",\"type\": \"int\"}]}",
                        false},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"aliases\":[\"Alias1\"], \"customprop\":\"123456\", \"fields\":[{\"name\":\"f1\",\"type\":\"long\", \"order\":\"ascending\", \"fprop\":\"faaa\" }, {\"name\":\"f2\",\"type\": \"int\"}]}",
                        true},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"aliases\":[\"Alias1\"], \"fields\":[{\"name\":\"f1\",\"type\":\"long\"}, {\"name\":\"f2\",\"type\": \"int\"}]}",
                        true},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"aliases\":[\"Alias1\",\"Alias2\"], \"fields\":[{\"name\":\"f1\",\"type\":\"long\"}, {\"name\":\"f2\",\"type\": \"int\"}]}",
                        true},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"aliases\":[\"Alias1\",9], \"fields\":[{\"name\":\"f1\",\"type\":\"long\"}, \"name\":\"f2\",\"type\": \"int\"}]}",
                        false},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"aliases\":[1, 2], \"fields\":[{\"name\":\"f1\",\"type\":\"long\", \"default\": \"100\"}, {\"name\":\"f2\",\"type\": \"int\"}]}",
                        false},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"aliases\": \"wrong alias format\", fields\":[{\"name\":\"value\",\"type\":\"long\", \"default\": \"100\"}, {\"name\":\"next\",\"type\":[\"LongList\",\"null\"]}]}",
                        false});
    }

    private final String schema;
    private final boolean valid;

    public SchemaAliasTest(String schema, boolean valid) {
        this.schema = schema;
        this.valid = valid;
    }

    @Test
    public void testAliases() { // also tests properties, default, order
        try {
            Schema sc = Schema.parse(schema);
            Assert.assertTrue(valid);

            String json = sc.toString();
            Schema sc2 = Schema.parse(json);
            String json2 = sc2.toString();

            Assert.assertEquals(json, json2);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.assertFalse(valid);
        }
    }
}
