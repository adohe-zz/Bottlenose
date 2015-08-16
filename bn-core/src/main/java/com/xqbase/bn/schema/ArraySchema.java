package com.xqbase.bn.schema;

import com.xqbase.bn.util.ObjectUtil;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;

/**
 * The Array Schema definition.
 *
 * @author Tony He
 */
public class ArraySchema extends UnnamedSchema {

    private final Schema itemSchema;

    protected ArraySchema(Schema itemSchema, PropertyMap propertyMap) {
        super(SchemaType.ARRAY, propertyMap);
        if (null == itemSchema) {
            throw new IllegalArgumentException("Array Item Schema can't be null");
        }
        this.itemSchema = itemSchema;
    }

    public Schema getItemSchema() {
        return itemSchema;
    }

    public static ArraySchema newInstance(JsonNode node, PropertyMap propMap,
            SchemaNames names) {
        JsonNode itemsNode = node.get("items");
        if (null == itemsNode) {
            throw new SchemaParseException("Array has no items type: " + node);
        }

        return new ArraySchema(parse(itemsNode, names), propMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ArraySchema)) return false;

        ArraySchema that = (ArraySchema) obj;
        return itemSchema.equals(that.itemSchema)
                && ObjectUtil.equals(getPropertyMap(), that.getPropertyMap());
    }

    @Override
    public int hashCode() {
        return 29 * itemSchema.hashCode() + ObjectUtil.hashCode(getPropertyMap());
    }

    @Override
    protected void writeJsonFields(JsonGenerator gen, SchemaNames names) throws IOException {
        gen.writeFieldName("items");
        itemSchema.writeJSON(gen, names);
    }
}
