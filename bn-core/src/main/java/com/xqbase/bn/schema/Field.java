package com.xqbase.bn.schema;

import org.codehaus.jackson.JsonNode;
import java.util.Set;

/**
 * This class represents a field within a record.
 *
 * @author Tony He
 */
public class Field {

    public enum SortOrder {
        ASCENDING,
        DESCENDING,
        IGNORE;

        private String name;
        private SortOrder() {
            this.name = this.name().toLowerCase();
        }
    }

    private final String name;

    private final Set<String> aliases;

    private final int pos;

    private final String doc;

    private final JsonNode defaultValue;

    private final SortOrder ordering;

    private final Schema schema;

    private final PropertyMap props;

    /**
     * Constructor for the field class
     *
     * @param schema       schema for the field type
     * @param name         name of the field
     * @param aliases      set of aliases for the name of the field
     * @param pos          position of the field
     * @param doc          documentation for the field
     * @param defaultValue field's default value if it exists
     * @param sortOrder    sort order of the field
     * @param props        properties map
     */
    public Field(Schema schema, String name, Set<String> aliases, int pos, String doc,
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

    public String getDoc() {
        return doc;
    }

    public int getPos() {
        return pos;
    }

    public Set<String> getAliases() {
        return aliases;
    }

    public SortOrder getOrdering() {
        return ordering;
    }

    public Schema getSchema() {
        return schema;
    }

    public JsonNode getDefaultValue() { return defaultValue; }
}
