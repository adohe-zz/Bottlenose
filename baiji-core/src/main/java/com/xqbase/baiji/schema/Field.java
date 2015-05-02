package com.xqbase.baiji.schema;

import com.xqbase.baiji.util.ObjectUtil;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents each field of {@link RecordSchema}.
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
     * Constructor for the field class
     *
     * @param schema       schema for the field type
     * @param name         name of the field
     * @param aliases      list of aliases for the name of the field
     * @param pos          position of the field
     * @param doc          documentation for the field
     * @param defaultValue field's default value if it exists
     * @param sortOrder    sort order of the field
     * @param props        property map
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

    public List<String> getAliases() {
        return aliases;
    }

    public int getPos() {
        return pos;
    }

    public String getDoc() {
        return doc;
    }

    public JsonNode getDefaultValue() {
        return defaultValue;
    }

    public SortOrder getOrdering() {
        return ordering;
    }

    public Schema getSchema() {
        return schema;
    }

    /**
     * Writes the Field class in JSON format
     *
     * @param node
     * @param names
     * @param encSpace
     */
    protected void writeJson(JsonGenerator node, SchemaNames names, String encSpace)
            throws IOException {
        node.writeStartObject();
        JsonHelper.writeIfNotNullOrEmpty(node, "name", name);
        JsonHelper.writeIfNotNullOrEmpty(node, "doc", doc);

        if (defaultValue != null) {
            node.writeFieldName("default");
            node.writeTree(defaultValue);
        }
        if (schema != null) {
            node.writeFieldName("type");
            schema.writeJson(node, names, encSpace);
        }

        if (props != null) {
            props.writeJson(node);
        }

        if (aliases != null) {
            node.writeFieldName("aliases");
            node.writeStartArray();
            for (String name : aliases) {
                node.writeString(name);
            }
            node.writeEndArray();
        }

        node.writeEndObject();
    }

    /**
     * Parses the 'aliases' property from the given JSON token
     *
     * @param node JSON object to read
     * @return List of string that represents the list of alias. If no 'aliases' specified, then it returns null.
     */
    static List<String> getAliases(JsonNode node) {
        JsonNode aliasesNode = node.get("aliases");
        if (aliasesNode == null) {
            return null;
        }

        if (!aliasesNode.isArray()) {
            throw new SchemaParseException("Aliases must be of format JSON array of strings");
        }

        List<String> aliases = new ArrayList<String>();
        for (JsonNode aliasNode : aliasesNode) {
            if (!aliasNode.isTextual()) {
                throw new SchemaParseException("Aliases must be of format JSON array of strings");
            }

            aliases.add(aliasNode.getTextValue());
        }
        return aliases;
    }

    /**
     * Returns the field's custom property value given the property name
     *
     * @param key
     * @return
     */
    public String getProp(String key) {
        if (props == null) {
            return null;
        }
        return props.get(key);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Field)) {
            return false;
        }
        Field that = (Field) obj;
        return ObjectUtil.equals(that.name, name) && that.pos == pos &&
                ObjectUtil.equals(that.doc, doc)
                && ObjectUtil.equals(that.ordering, ordering) &&
                defaultValueEquals(that.defaultValue)
                && that.schema.equals(schema) && ObjectUtil.equals(that.props, props);
    }

    private boolean defaultValueEquals(JsonNode thatDefaultValue) {
        if (defaultValue == null) {
            return thatDefaultValue == null;
        }
        if (Double.isNaN(defaultValue.getDoubleValue())) {
            return Double.isNaN(thatDefaultValue.getDoubleValue());
        }
        return defaultValue.equals(thatDefaultValue);
    }

    @Override
    public int hashCode() {
        return 17 * name.hashCode() + pos + 19 * ObjectUtil.hashCode(doc) +
                23 * ObjectUtil.hashCode(ordering) + 29 * ObjectUtil.hashCode(defaultValue) +
                31 * schema.hashCode() + 37 * ObjectUtil.hashCode(props);
    }
}
