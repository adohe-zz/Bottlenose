package com.xqbase.bn.io;

import com.xqbase.bn.exceptions.BaijiTypeException;
import com.xqbase.bn.io.parsing.JsonGrammarGenerator;
import com.xqbase.bn.io.parsing.Parser;
import com.xqbase.bn.io.parsing.Symbol;
import com.xqbase.bn.schema.Schema;
import com.xqbase.bn.util.Utf8;
import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.util.DefaultPrettyPrinter;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.BitSet;
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

    private final Parser parser;
    private JsonGenerator out;

    protected BitSet isEmpty = new BitSet();

    public JsonEncoder(Schema sc, OutputStream out) throws IOException {
        this(sc, out, false);
    }

    public JsonEncoder(Schema sc, OutputStream out, boolean pretty) throws IOException {
        this(sc, getJsonGenerator(out, pretty));
    }

    public JsonEncoder(Schema sc, JsonGenerator jsonGenerator) throws IOException {
        configure(jsonGenerator);
        this.parser = new Parser(new JsonGrammarGenerator().generate(sc), this);
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

    /**
     * Reconfigure this JsonEncoder to output to the JsonGenerator provided.
     * * <p/>
     * If the JsonGenerator provided is null, a NullPointerException is thrown.
     * <p/>
     * Otherwise, this JsonEncoder will flush its current output and then
     * reconfigure its output to use the provided JsonGenerator.
     */
    private JsonEncoder configure(JsonGenerator out) throws IOException {
        if (null == out)
            throw new NullPointerException("JsonGenerator can't be null");
        if (parser != null) {
            flush();
        }
        this.out = out;
        return this;
    }

    /**
     * Reconfigures this JsonEncoder to use the output stream provided.
     * <p/>
     * If the OutputStream provided is null, a NullPointerException is thrown.
     * <p/>
     * Otherwise, this JsonEncoder will flush its current output and then
     * reconfigure its output to use a default UTF8 JsonGenerator that writes
     * to the provided OutputStream.
     *
     * @param out
     *          The OutputStream to direct output to. Cannot be null.
     * @throws IOException
     * @return this JsonEncoder
     */
    public JsonEncoder configure(OutputStream out) throws IOException {
        this.configure(getJsonGenerator(out, false));
        return this;
    }

    @Override
    public Symbol doAction(Symbol input, Symbol top) throws IOException {
        if (top instanceof Symbol.FieldAdjustAction) {
            Symbol.FieldAdjustAction fa = (Symbol.FieldAdjustAction) top;
            out.writeFieldName(fa.fname);
        } else if (top == Symbol.RECORD_START) {
            out.writeStartObject();
        } else if (top == Symbol.RECORD_END || top == Symbol.UNION_END) {
            out.writeEndObject();
        } else if (top != Symbol.FIELD_END) {
            throw new BaijiTypeException("Unknown action symbol " + top);
        }
        return null;
    }

    @Override
    public void writeNull() throws IOException {
        parser.advance(Symbol.NULL);
        out.writeNull();
    }

    @Override
    public void writeBoolean(boolean b) throws IOException {
        parser.advance(Symbol.BOOLEAN);
        out.writeBoolean(b);
    }

    @Override
    public void writeInt(int n) throws IOException {
        parser.advance(Symbol.INT);
        out.writeNumber(n);
    }

    @Override
    public void writeLong(long n) throws IOException {
        parser.advance(Symbol.LONG);
        out.writeNumber(n);
    }

    @Override
    public void writeFloat(float f) throws IOException {
        parser.advance(Symbol.FLOAT);
        out.writeNumber(f);
    }

    @Override
    public void writeDouble(double d) throws IOException {
        parser.advance(Symbol.DOUBLE);
        out.writeNumber(d);
    }

    @Override
    public void writeString(Utf8 utf8) throws IOException {
        writeString(utf8.toString());
    }

    @Override
    public void writeString(String str) throws IOException {
        parser.advance(Symbol.STRING);
        if (parser.topSymbol() == Symbol.MAP_KEY_MARKER) {
            parser.advance(Symbol.MAP_KEY_MARKER);
            out.writeFieldName(str);
        } else {
            out.writeString(str);
        }
    }

    @Override
    public void writeString(CharSequence charSequence) throws IOException {
        if (charSequence instanceof Utf8) {
            writeString((Utf8) charSequence);
        } else {
            writeString(charSequence.toString());
        }
    }

    @Override
    public void writeBytes(byte[] bytes, int start, int len) throws IOException {
        parser.advance(Symbol.BYTES);
        writeByteArray(bytes, start, len);
    }

    private void writeByteArray(byte[] bytes, int start, int len) throws IOException {
        out.writeString(new String(bytes, start, len, Charset.forName("UTF-8")));
    }

    @Override
    public void writeBytes(ByteBuffer bytes) throws IOException {
        if (bytes.hasArray()) {
            writeBytes(bytes.array(), bytes.position(), bytes.remaining());
        } else {
            byte[] b = new byte[bytes.remaining()];
            bytes.duplicate().get(b);
            writeBytes(b, 0, b.length);
        }
    }

    @Override
    public void writeFixed(byte[] bytes, int start, int len) throws IOException {
        // no implementation
    }

    @Override
    public void writeDatetime(Calendar date) throws IOException {
        parser.advance(Symbol.DATETIME);
        out.writeNumber(date.getTimeInMillis());
    }

    @Override
    public void writeEnum(int e) throws IOException {
        parser.advance(Symbol.ENUM);
        Symbol.EnumLabelsAction top = (Symbol.EnumLabelsAction) parser.popSymbol();
        if (e < 0 || e > top.size) {
            throw new BaijiTypeException(
                    "Enumeration out of range: max is " +
                            top.size + " but received " + e
            );
        }
        out.writeString(top.getLabel(e));
    }

    @Override
    public void writeArrayStart() throws IOException {
        parser.advance(Symbol.ARRAY_START);
        out.writeStartArray();
        push();
        isEmpty.set(depth());
    }

    @Override
    public void startItem() throws IOException {
        if (!isEmpty.get(pos)) {
            parser.advance(Symbol.ITEM_END);
        }
        super.startItem();
        isEmpty.clear(depth());
    }

    @Override
    public void writeArrayEnd() throws IOException {
        if (!isEmpty.get(pos)) {
            parser.advance(Symbol.ITEM_END);
        }
        pop();
        parser.advance(Symbol.ARRAY_END);
        out.writeEndArray();
    }

    @Override
    public void writeMapStart() throws IOException {
        push();
        isEmpty.set(depth());

        parser.advance(Symbol.MAP_START);
        out.writeStartObject();
    }

    @Override
    public void writeMapEnd() throws IOException {
        if (!isEmpty.get(pos)) {
            parser.advance(Symbol.ITEM_END);
        }
        pop();

        parser.advance(Symbol.MAP_END);
        out.writeEndObject();
    }

    @Override
    public void writeUnionIndex(int unionIndex) throws IOException {
        parser.advance(Symbol.UNION);
        Symbol.Alternative top = (Symbol.Alternative) parser.popSymbol();
        Symbol symbol = top.getSymbol(unionIndex);
        if (symbol != Symbol.NULL) {
            out.writeStartObject();
            out.writeFieldName(top.getLabel(unionIndex));
            parser.pushSymbol(Symbol.UNION_END);
        }
        parser.pushSymbol(symbol);
    }

    @Override
    public void flush() throws IOException {
        parser.processImplicitActions();
        if (out != null) {
            out.flush();
        }
    }
}
