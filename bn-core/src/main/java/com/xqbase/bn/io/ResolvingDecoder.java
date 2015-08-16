package com.xqbase.bn.io;

import com.xqbase.bn.io.parsing.BinaryGrammarGenerator;
import com.xqbase.bn.io.parsing.Symbol;
import com.xqbase.bn.schema.Schema;
import com.xqbase.bn.util.Utf8;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * {@link com.xqbase.bn.io.Decoder} that performs type-resolution.
 *
 * @author Tony He
 */
public class ResolvingDecoder extends ValidatingDecoder {

    private Decoder backup;

    public ResolvingDecoder(Schema schema, Decoder in) throws IOException {
        this(resolve(schema), in);
    }

    public ResolvingDecoder(Object resolver, Decoder in) throws IOException {
        super((Symbol) resolver, in);
    }

    /**
     * Produces an opaque resolver that can be used to construct a new
     * {@link ResolvingDecoder#ResolvingDecoder(Object, Decoder)}. The
     * returned Object is immutable and hence can be simultaneously used
     * in many ResolvingDecoders. This method is reasonably expensive, the
     * users are encouraged to cache the result.
     *
     * @param schema  The writer's schema. Cannot be null.
     * @return  The opaque resolver.
     * @throws IOException
     */
    public static Object resolve(Schema schema) {
        if (null == schema) {
            throw new NullPointerException("schema cannot be null");
        }
        return new BinaryGrammarGenerator().generate(schema);
    }

    @Override
    public Symbol doAction(Symbol input, Symbol top) throws IOException {
        return null;
    }

    @Override
    public void readNull() throws IOException {

    }

    @Override
    public boolean readBoolean() throws IOException {
        return false;
    }

    @Override
    public int readInt() throws IOException {
        return 0;
    }

    @Override
    public long readLong() throws IOException {
        return 0;
    }

    @Override
    public float readFloat() throws IOException {
        return 0;
    }

    @Override
    public double readDouble() throws IOException {
        return 0;
    }

    @Override
    public byte[] readBytes() throws IOException {
        return new byte[0];
    }

    @Override
    public ByteBuffer readBytes(ByteBuffer old) throws IOException {
        return null;
    }

    @Override
    public void readFixed(byte[] bytes, int start, int length) throws IOException {

    }

    @Override
    public void readFixed(byte[] bytes) throws IOException {

    }

    @Override
    public String readString(Utf8 old) throws IOException {
        return null;
    }

    @Override
    public String readString() throws IOException {
        return null;
    }

    @Override
    public Calendar readDatetime() throws IOException {
        return null;
    }

    @Override
    public int readEnum() throws IOException {
        return 0;
    }

    @Override
    public long readArrayStart() throws IOException {
        return 0;
    }

    @Override
    public long readArrayNext() throws IOException {
        return 0;
    }

    @Override
    public long readMapStart() throws IOException {
        return 0;
    }

    @Override
    public long readMapNext() throws IOException {
        return 0;
    }

    @Override
    public int readUnionIndex() throws IOException {
        return 0;
    }

    @Override
    public void skipAction() throws IOException {

    }

    @Override
    public void skipTopSymbol() throws IOException {

    }
}
