package com.xqbase.bn.schema;

import com.xqbase.bn.common.util.StringUtils;
import com.xqbase.bn.util.ObjectUtil;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
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
        if (!StringUtils.hasLength(name)) {
            throw new IllegalArgumentException("name cannot be null.");
        }
        if (null == schema) {
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
        if (null == defaultValue) {
            return null == thatDefaultValue;
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

    /**
     * Write field in JSON Format.
     */
    protected void writeJSON(JsonGenerator writer, SchemaNames names) throws IOException {
        writer.writeStartObject();
        JsonHelper.writeIfNotNullOrEmpty(writer, "name", name);
        JsonHelper.writeIfNotNullOrEmpty(writer, "doc", doc);

        if (defaultValue != null) {
            writer.writeFieldName("default");
            writer.writeTree(defaultValue);
        }

        if (ordering != null) {
            writer.writeStringField("order", ordering.name);
        }

        writer.writeFieldName("type");
        schema.writeJSON(writer, names);

        if (props != null) {
            props.writeJSON(writer);
        }

        if (aliases != null && aliases.size() > 0) {
            writer.writeFieldName("aliases");
            writer.writeStartArray();
            for (String alias : aliases) {
                writer.writeString(alias);
            }
            writer.writeEndArray();
        }
        writer.writeEndObject();
    }
}
