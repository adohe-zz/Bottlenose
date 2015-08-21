package com.xqbase.bn.generic;

import com.xqbase.bn.exceptions.BaijiRuntimeException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class GenericTestResolutionSimple extends GenericTestBase {

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[]{"[{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}, \"string\"]", "{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}", "s1", true},
                new Object[]{"[{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}, \"string\"]", "{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}", "s2", true},
                new Object[]{"[{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}, \"string\"]", "{\"type\": \"enum\", \"symbols\": [\"s1\", \"s2\"], \"name\": \"e\"}", "s3", false});
    }

    private final String unionSchema;
    private final String enumSchema;
    private final String value;
    private final boolean valid;

    public GenericTestResolutionSimple(String unionSchema, String enumSchema, String value, boolean valid) {
        this.unionSchema = unionSchema;
        this.enumSchema = enumSchema;
        this.value = value;
        this.valid = valid;
    }

    @Test
    public void testResolutionSimple() throws IOException {
        try {
            test(unionSchema, makeEnum(enumSchema, value));
        } catch (BaijiRuntimeException ex) {
            ex.printStackTrace();
            Assert.assertFalse(valid);
        }
    }
}
