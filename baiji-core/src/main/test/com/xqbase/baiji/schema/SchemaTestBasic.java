package com.xqbase.baiji.schema;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SchemaTestBasic extends SchemaTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(// Primitive types - shorthand
                new Object[]{"null", true},
                new Object[]{"boolean", true},
                new Object[]{"int", true},
                new Object[]{"long", true},
                new Object[]{"float", true},
                new Object[]{"double", true},
                new Object[]{"bytes", true},
                new Object[]{"string", true},
                new Object[]{"\"null\"", true},
                new Object[]{"\"boolean\"", true},
                new Object[]{"\"int\"", true},
                new Object[]{"\"long\"", true},
                new Object[]{"\"float\"", true},
                new Object[]{"\"double\"", true},
                new Object[]{"\"bytes\"", true},
                new Object[]{"\"string\"", true},

                // Primitive types - longer
                new Object[]{"{ \"type\": \"null\" }", true},
                new Object[]{"{ \"type\": \"boolean\" }", true},
                new Object[]{"{ \"type\": \"int\" }", true},
                new Object[]{"{ \"type\": \"long\" }", true},
                new Object[]{"{ \"type\": \"float\" }", true},
                new Object[]{"{ \"type\": \"double\" }", true},
                new Object[]{"{ \"type\": \"bytes\" }", true},
                new Object[]{"{ \"type\": \"string\" }", true},

                // Record
                new Object[]{"{\"type\": \"record\",\"name\": \"Test\",\"fields\": [{\"name\": \"f\",\"type\": \"long\"}]}", true},
                new Object[]{"{\"type\": \"record\",\"name\": \"Test\",\"fields\": " +
                        "[{\"name\": \"f1\",\"type\": \"long\"},{\"name\": \"f2\", \"type\": \"int\"}]}", true},
                new Object[]{"{\"type\": \"error\",\"name\": \"Test\",\"fields\": " +
                        "[{\"name\": \"f1\",\"type\": \"long\"},{\"name\": \"f2\", \"type\": \"int\"}]}", true},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"value\",\"type\":\"long\"},{\"name\":\"next\",\"type\":[\"LongList\",\"null\"]}]}"
                        , true}, // Recursive.
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"," +
                        "\"fields\":[{\"name\":\"value\",\"type\":\"long\"},{\"name\":\"next\",\"type\":[\"LongListA\",\"null\"]}]}", false},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\"}", false},
                new Object[]{"{\"type\":\"record\",\"name\":\"LongList\", \"fields\": \"hi\"}", false},

                // Enum
                new Object[]{"{\"type\": \"enum\", \"name\": \"Test\", \"symbols\": [\"A\", \"B\"]}", true},
                new Object[]{"{\"type\": \"enum\", \"name\": \"Status\", \"symbols\": \"Normal Caution Critical\"}", false},
                new Object[]{"{\"type\": \"enum\", \"name\": [ 0, 1, 1, 2, 3, 5, 8 }, \"symbols\": [\"Golden\", \"Mean\"]}", false},
                new Object[]{"{\"type\": \"enum\", \"symbols\" : [\"I\", \"will\", \"fail\", \"no\", \"name\"]}", false},
                new Object[]{"{\"type\": \"enum\", \"name\": \"Test\", \"symbols\" : [\"AA\", \"AA\"]}", false},

                // Array
                new Object[]{"{\"type\": \"array\", \"items\": \"long\"}", true},
                new Object[]{
                        "{\"type\": \"array\",\"items\": {\"type\": \"enum\", \"name\": \"Test\", \"symbols\": [\"A\", \"B\"]}}", true},

                // Map
                new Object[]{"{\"type\": \"map\", \"values\": \"long\"}", true},
                new Object[]{
                        "{\"type\": \"map\",\"values\": {\"type\": \"enum\", \"name\": \"Test\", \"symbols\": [\"A\", \"B\"]}}", true},

                // Union
                new Object[]{"[\"string\", \"null\", \"long\"]", true},
                new Object[]{"[\"string\", \"long\", \"long\"]", false},
                new Object[]{"[{\"type\": \"array\", \"items\": \"long\"}, {\"type\": \"array\", \"items\": \"string\"}]", false});
    }

    private final String schema;
    private final boolean valid;

    public SchemaTestBasic(String schema, boolean valid) {
        this.schema = schema;
        this.valid = valid;
    }

    @Test
    public void testBasic() {
        try {
            Schema.parse(schema);
            Assert.assertTrue(valid);
        } catch (SchemaParseException ex) {
            if (valid) {
                ex.printStackTrace();
            }
            Assert.assertFalse(valid);
        }
    }
}
