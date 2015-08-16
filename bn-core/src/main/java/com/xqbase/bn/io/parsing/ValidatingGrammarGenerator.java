package com.xqbase.bn.io.parsing;

import com.xqbase.bn.schema.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The class that generates validating grammar.
 */
public class ValidatingGrammarGenerator {

    /**
     * Returns the non-terminal that is the start symbol
     * for the grammar for the given schema <tt>schema</tt>.
     */
    public Symbol generate(Schema schema) {
        return Symbol.root(generate(schema, new HashMap<LitS, Symbol>()));
    }

    /**
     * Returns the non-terminal that is the start symbol
     * for the grammar for the given schema <tt>sc</tt>. If there is already an entry
     * for the given schema in the given map <tt>seen</tt> then
     * that entry is returned. Otherwise a new symbol is generated and
     * an entry is inserted into the map.
     * @param sc    The schema for which the start symbol is required
     * @param seen  A map of schema to symbol mapping done so far.
     * @return      The start symbol for the schema
     */
    public Symbol generate(Schema sc, Map<LitS, Symbol> seen) {
        switch (sc.getType()) {
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
            case ENUM:
                return Symbol.seq(Symbol.intCheckAction(((EnumSchema) sc).getEnumSymbols().size()),
                        Symbol.ENUM);
            case ARRAY:
                return Symbol.seq(Symbol.repeat(Symbol.ARRAY_END, generate(((ArraySchema) sc).getItemSchema(), seen)),
                        Symbol.ARRAY_START);
            case MAP:
                return Symbol.seq(Symbol.repeat(Symbol.MAP_END,
                                generate(((MapSchema) sc).getValueSchema(), seen), Symbol.STRING),
                        Symbol.MAP_START);
            case RECORD: {
                RecordSchema rsc = (RecordSchema) sc;
                LitS wsc = new LitS(sc);
                Symbol result = seen.get(wsc);
                if (null == result) {
                    Symbol[] production = new Symbol[rsc.getFields().size()];

                    /**
                     * We construct a symbol without filling the array. Please see
                     * {@link Symbol#production} for the reason.
                     */
                    result = Symbol.seq(production);
                    seen.put(wsc, result);

                    int i = production.length;
                    for (Field f : rsc.getFields()) {
                        production[--i] = generate(f.getSchema(), seen);
                    }
                }
                return result;
            }
            case UNION:
                UnionSchema usc = (UnionSchema) sc;
                List<Schema> subs = usc.getSchemas();
                Symbol[] symbols = new Symbol[subs.size()];
                String[] labels = new String[subs.size()];

                int i = 0;
                for (Schema b : subs) {
                    symbols[i] = generate(b, seen);
                    labels[i] = b.getName();
                    i++;
                }
                return Symbol.seq(Symbol.alt(symbols, labels), Symbol.UNION);
            default:
                throw new RuntimeException("Unexpected schema type");
        }
    }

    /** A wrapper around Schema that does "==" equality. */
    static class LitS {
        public final Schema actual;
        public LitS(Schema actual) { this.actual = actual; }

        /**
         * Two LitS are equal if and only if their underlying schema is
         * the same (not merely equal).
         */
        public boolean equals(Object o) {
            if (o == this) return true;
            if (!(o instanceof LitS)) return false;

            LitS that = (LitS) o;
            return actual == that.actual;
        }

        public int hashCode() {
            return actual.hashCode();
        }
    }
}
