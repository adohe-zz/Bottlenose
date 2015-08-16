package com.xqbase.bn.schema;

import com.xqbase.bn.exceptions.BaijiRuntimeException;
import org.junit.Assert;


public abstract class SchemaTestBase {

    protected static void testEquality(String s, Schema sc) {
        Assert.assertTrue(sc.equals(sc));
        Schema sc2 = Schema.parse(s);
        Assert.assertTrue(sc.equals(sc2));
        Assert.assertEquals(sc.hashCode(), sc2.hashCode());
    }

    protected static void testToString(Schema sc) {
        try {
            Assert.assertEquals(sc, Schema.parse(sc.toString()));
        } catch (Exception e) {
            throw new BaijiRuntimeException(e + ": " + sc);
        }
    }
}
