package com.xqbase.baiji.io.parsing;

import com.xqbase.baiji.schema.Schema;

import java.util.HashMap;
import java.util.Map;

/**
 * The class that generates a grammar suitable to parse Baiji data
 * in JSON format.
 */
public class JsonGrammarGenerator extends ValidatingGrammarGenerator {

    /**
     * Returns the non-terminal that is the start symbol
     * for the grammar for the grammar for the given schema <tt>sc</tt>.
     */
    @Override
    public Symbol generate(Schema schema) {
        return Symbol.root(generate(schema, new HashMap<LitS, Symbol>()));
    }

    @Override
    public Symbol generate(Schema sc, Map<LitS, Symbol> seen) {
        switch (sc.getType()) {
            case NULL:
            case BOOLEAN:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
            case STRING:
            case BYTES:
            case UNION:
                return super.generate(sc, seen);
        }
        return super.generate(sc, seen);
    }
}
