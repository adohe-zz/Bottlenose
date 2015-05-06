package com.xqbase.baiji.schema;

import org.codehaus.jackson.JsonNode;

import java.util.List;

/**
 * This class represents each field of the schema fields array.
 *
 * @author Tony He
 */
public class Field {

    public enum SortOrder {
        ASCENDING,
        DESCENDING,
        IGNORE
    }

    private final String name;

    private final List<String> aliases;

    private final int pos;

    private final String doc;

    private final JsonNode defaultValue;

    private final SortOrder ordering;

    private final Schema schema;

    private final PropertyMap props;

    /**
     * Constructor for the field class.
     *
     * @param schema       schema for the field type
     * @param name         name of the field
     * @param aliases      list of aliases for the name of the field
     * @param pos          position of the field
     * @param doc          documentation for the field
     * @param defaultValue field's default value if it exists
     * @param sortOrder    sort order of the field
     * @param props        properties map
     */
    public Field(Schema schema, String name, List<String> aliases, int pos, String doc,
                 JsonNode defaultValue, SortOrder sortOrder, PropertyMap props) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        if (schema == null) {
            throw new IllegalArgumentException("schema cannot be null.");
        }
        this.schema = schema;
        this.name = name;
        this.aliases = aliases;
        this.pos = pos;
        this.doc = doc;
        this.defaultValue = defaultValue;
        this.ordering = sortOrder;
        this.props = props;
    }

    public String getName() {
        return name;
    }

    public PropertyMap getProps() {
        return props;
    }

    public Schema getSchema() {
        return schema;
    }

    public SortOrder getOrdering() {
        return ordering;
    }

    public JsonNode getDefaultValue() {
        return defaultValue;
    }

    public String getDoc() {
        return doc;
    }

    public int getPos() {
        return pos;
    }

    public List<String> getAliases() {
        return aliases;
    }
}
