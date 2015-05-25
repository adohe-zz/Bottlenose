package com.xqbase.baiji.io;

import com.xqbase.baiji.io.parsing.Parser;
import com.xqbase.baiji.io.parsing.SkipParser;
import com.xqbase.baiji.io.parsing.Symbol;

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
