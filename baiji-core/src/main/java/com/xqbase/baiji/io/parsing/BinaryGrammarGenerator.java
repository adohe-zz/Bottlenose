package com.xqbase.baiji.io.parsing;

import com.xqbase.baiji.exceptions.BaijiTypeException;
import com.xqbase.baiji.schema.Schema;
import com.xqbase.baiji.schema.SchemaType;
import java.util.Map;

/**
 * The class that generates a grammar suitable to parse Baiji data
 * in Binary format.
 *
 * @author Tony He
 */
public class BinaryGrammarGenerator extends ValidatingGrammarGenerator {

    public BinaryGrammarGenerator(Schema schema) {

    }

    /**
     * Resolves the reader schema and returns the start symbol for the grammar generated.
     * If there is already a symbol in the map <tt>seen</tt> for resolving the
     * two schemas, then that symbol is returned. Otherwise a new symbol is
     * generated and returned.
     * @param schema    The schema used by the reader
     * @param seen      The &lt;reader-schema, writer-schema&gt; to symbol
     * map of start symbols of resolving grammars so far.
     * @return          The start symbol for the resolving grammar
     */
    public Symbol generate(Schema schema, Map<LitS, Symbol> seen) {
        final SchemaType type = schema.getType();
        switch (type) {
            case NULL:
                return Symbol.NULL;
            case BOOLEAN:
                return Symbol.BOOLEAN;
            case INT:
                return Symbol.INT;
            case LONG:
                return Symbol.LONG;
            case FLOAT:
                return Symbol.FLOAT;
            case DOUBLE:
                return Symbol.DOUBLE;
            case STRING:
                return Symbol.STRING;
            case BYTES:
                return Symbol.BYTES;
            case DATETIME:
                return Symbol.DATETIME;
            default:
                throw new BaijiTypeException("unknown type: " + type);
        }
    }
}
