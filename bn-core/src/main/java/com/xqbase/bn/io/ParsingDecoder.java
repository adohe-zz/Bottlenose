package com.xqbase.bn.io;

import com.xqbase.bn.io.parsing.Parser;
import com.xqbase.bn.io.parsing.SkipParser;
import com.xqbase.bn.io.parsing.Symbol;

import java.io.IOException;

/**
 * Base class for parser based decoders.
 *
 * @author Tony He
 */
public abstract class ParsingDecoder implements Decoder,
                    Parser.ActionHandler, SkipParser.SkipHandler {

    protected final SkipParser skipParser;

    protected ParsingDecoder(Symbol root) throws IOException {
        this.skipParser = new SkipParser(root, this, this);
    }
}
