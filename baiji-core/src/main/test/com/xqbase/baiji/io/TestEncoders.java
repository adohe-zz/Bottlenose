package com.xqbase.baiji.io;

import com.xqbase.baiji.schema.Schema;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

public class TestEncoders {

    @Test
    public void testJsonEncoderInit() throws Exception {
        Schema s = Schema.parse("\"int\"");
        OutputStream out = new ByteArrayOutputStream();
        JsonEncoder enc = new JsonEncoder(s, new JsonFactory().createJsonGenerator(out, JsonEncoding.UTF8));
        enc.configure(out);
    }
}
