package com.xqbase.baiji.io;

import com.xqbase.baiji.io.parsing.Parser;
import com.xqbase.baiji.io.parsing.Symbol;
import com.xqbase.baiji.schema.Schema;
import com.xqbase.baiji.util.Utf8;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * An Encoder for Baiji's JSON data encoding.
 *
 * JsonEncoder is not thread safe.
 *
 * @author Tony He
 */
public class JsonEncoder extends ParsingEncoder implements Parser.ActionHandler {

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public JsonEncoder(Schema sc, OutputStream out) throws IOException {
        this(sc, getJsonGenerator(out, false));
    }

    public JsonEncoder(Schema sc, OutputStream out, boolean pretty) throws IOException {
        this(sc, getJsonGenerator(out, pretty));
    }

    public JsonEncoder(Schema sc, JsonGenerator jsonGenerator) {

    }

    private static JsonGenerator getJsonGenerator(OutputStream out, boolean pretty) throws IOException {
        if (null == out) {
            throw new NullPointerException("OutputStream cannot be null");
        }
        JsonGenerator g = new JsonFactory().createJsonGenerator(out, JsonEncoding.UTF8);
        if (pretty) {
            DefaultPrettyPrinter pp = new DefaultPrettyPrinter() {
                @Override
                public void writeRootValueSeparator(JsonGenerator jg) throws IOException {
                    jg.writeRaw(LINE_SEPARATOR);
                }
            };
            g.setPrettyPrinter(pp);
        } else {
            MinimalPrettyPrinter pp = new MinimalPrettyPrinter();
            pp.setRootValueSeparator(LINE_SEPARATOR);
            g.setPrettyPrinter(pp);
        }

        return g;
    }

    @Override
    public Symbol doAction(Symbol input, Symbol top) throws IOException {
        return null;
    }

    @Override
    public void writeNull() throws IOException {

    }

    @Override
    public void writeBoolean(boolean b) throws IOException {

    }

    @Override
    public void writeInt(int n) throws IOException {

    }

    @Override
    public void writeLong(long n) throws IOException {

    }

    @Override
    public void writeFloat(float f) throws IOException {

    }

    @Override
    public void writeDouble(double d) throws IOException {

    }

    @Override
    public void writeString(Utf8 str) throws IOException {

    }

    @Override
    public void writeString(String str) throws IOException {

    }

    @Override
    public void writeString(CharSequence charSequence) throws IOException {

    }

    @Override
    public void writeBytes(byte[] bytes, int start, int len) throws IOException {

    }

    @Override
    public void writeBytes(ByteBuffer bytes) throws IOException {

    }

    @Override
    public void writeFixed(byte[] bytes, int start, int len) throws IOException {

    }

    @Override
    public void writeDatetime(Calendar date) throws IOException {

    }

    @Override
    public void writeEnum(int e) throws IOException {

    }

    @Override
    public void writeArrayStart() throws IOException {

    }

    @Override
    public void setItemCount(long itemCount) throws IOException {

    }

    @Override
    public void startItem() throws IOException {

    }

    @Override
    public void writeArrayEnd() throws IOException {

    }

    @Override
    public void writeMapStart() throws IOException {

    }

    @Override
    public void writeMapEnd() throws IOException {

    }

    @Override
    public void writeUnionIndex(int unionIndex) throws IOException {

    }

    @Override
    public void flush() throws IOException {

    }
}
