package com.xqbase.baiji.io;

import com.xqbase.baiji.io.parsing.Parser;
import com.xqbase.baiji.io.parsing.Symbol;
import com.xqbase.baiji.util.Utf8;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;

/**
 * An implementation of {@link com.xqbase.baiji.io.Decoder}
 * that ensures a sequence of operations conform to a schema.
 *
 * @author Tony He
 */
public class ValidatingDecoder extends ParsingDecoder
            implements Parser.ActionHandler {

    protected Decoder in;

    public ValidatingDecoder(Symbol root, Decoder in) throws IOException {
        super(root);
        this.configure(in);
    }

    // Configure the wrapped decoder.
    public ValidatingDecoder configure(Decoder in) {
        this.skipParser.reset();
        this.in = in;
        return this;
    }

    public void drain() throws IOException {
        skipParser.processImplicitActions();
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
