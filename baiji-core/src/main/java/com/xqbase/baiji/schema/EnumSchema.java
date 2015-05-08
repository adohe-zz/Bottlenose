package com.xqbase.baiji.schema;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.*;

/**
 * The EnumSchema Definition.
 *
 * @author Tony He
 */
public class EnumSchema extends NamedSchema implements Iterable<String> {

    private final List<String> symbols;
    private final Map<String, Integer[]> ordinals;

    /**
     * Construct a enum schema.
     *
     * @param schemaName the schema name.
     * @param doc        the schema doc.
     * @param props      the schema properties map.
     * @param names      list of named schemas already read
     */
    private EnumSchema(SchemaName schemaName, String doc, Set<SchemaName> aliases, Map.Entry<String, Integer>[] symbolsMap,
                PropertyMap props, SchemaNames names) {
        super(SchemaType.ENUM, schemaName, doc, aliases, props, names);

        symbols = new ArrayList<>();
        Map<String, Integer[]> symbolMap = new HashMap<String, Integer[]>();
        int lastValue = -1;
        for (Map.Entry<String, Integer> symbol : symbolsMap) {
            Integer[] values = new Integer[2];
            if (symbol.getValue() != null) {
                values[0] = values[1] = lastValue = symbol.getValue();
            } else {
                values[1] = ++lastValue;
            }
            symbols.add(symbol.getKey());
            symbolMap.put(symbol.getKey(), values);
        }
        this.ordinals = symbolMap;
    }

    /**
     * Constructor for enum schema
     *
     * @param name      name of enum
     * @param doc       documentation to the user of this schema
     * @param aliases   list of aliases for the name
     * @param symbols   list of enum symbols
     * @param symbolMap map of enum symbols and value
     *                  The first element of value is the explicit value which can be null, the second one is the actual value.
     * @param props     properties map
     * @param names     list of named schema already read
     */
    private EnumSchema(SchemaName name, String doc, Set<SchemaName> aliases, List<String> symbols,
                Map<String, Integer[]> symbolMap, PropertyMap props, SchemaNames names) {
        super(SchemaType.ENUM, name, doc, aliases, props, names);

        if (null == name.getName()) {
            throw new SchemaParseException("name cannot be null for enum schema.");
        }
        this.symbols = symbols;
        this.ordinals = symbolMap;
    }

    /**
     * Static function to return new instance of EnumSchema
     *
     * @param enumSchema    JSON object for enum schema
     * @param name          schema name
     * @param doc           documentation to the user of this schema
     * @param props         properties map
     * @param names         list of named schema already parsed in
     * @return a new instance of {@link EnumSchema}
     */
    protected static EnumSchema newInstance(JsonNode enumSchema, SchemaName name, String doc,
                PropertyMap props, SchemaNames names) {

        JsonNode symbolsNode = enumSchema.get("symbols");
        if (null == symbolsNode || !symbolsNode.isArray()) {
            throw new SchemaParseException("Enum has no symbols: "+ enumSchema);
        }

        List<String> symbols = new ArrayList<String>();
        Map<String, Integer[]> symbolMap = new HashMap<String, Integer[]>();
        int lastValue = -1;
        for (JsonNode n : symbolsNode) {
            Integer explicitValue = null;
            Integer actualValue;
            String symbol;
            if (n.isTextual()) {
                symbol = n.getTextValue();
                actualValue = ++lastValue;
            } else if (n.isObject()) {
                ObjectNode symbolObj = (ObjectNode) n;
                JsonNode symbolNameNode = symbolObj.get("name");
                if (null == symbolNameNode) {
                    throw new SchemaParseException("Missing symbol name: " + n);
                }
                if (!symbolNameNode.isTextual()) {
                    throw new SchemaParseException("Symbol name must be a string: " + n);
                }
                symbol = symbolNameNode.getTextValue();
                JsonNode valueNode = symbolObj.get("value");
                if (valueNode != null) {
                    if (!valueNode.isInt()) {
                        throw new SchemaParseException("Only integer value is allowed for an enum symbol: " + n);
                    }
                    explicitValue = valueNode.getIntValue();
                }
                lastValue = actualValue = explicitValue != null ? explicitValue : lastValue + 1;
            } else {
                throw new SchemaParseException("Invalid symbol object: " + n);
            }
            symbolMap.put(symbol, new Integer[]{explicitValue, actualValue});
            symbols.add(symbol);
        }

        return new EnumSchema(name, doc, getAliases(enumSchema, name.getNameSpace()), symbols, symbolMap, props, names);
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }
}
