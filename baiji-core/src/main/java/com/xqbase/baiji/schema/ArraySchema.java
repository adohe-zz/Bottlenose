package com.xqbase.baiji.schema;

import com.xqbase.baiji.exceptions.BaijiTypeException;
import com.xqbase.baiji.util.ObjectUtil;
import org.codehaus.jackson.JsonNode;

/**
 * The Array Schema definition.
 *
 * @author Tony He
 */
public class ArraySchema extends UnnamedSchema {

    private final Schema itemSchema;


    protected ArraySchema(Schema itemSchema, PropertyMap propertyMap) {
        super(SchemaType.ARRAY, propertyMap);
        if (itemSchema == null) {
            throw new IllegalArgumentException("Array Item Schema can't be null");
        }
        this.itemSchema = itemSchema;
    }

    public Schema getItemSchema() {
        return itemSchema;
    }

    public static ArraySchema newInstance(JsonNode node, PropertyMap propMap,
                    SchemaNames names, String encSpace) {
        JsonNode itemsNode = node.get("items");
        if (itemsNode == null) {
            throw new BaijiTypeException("Array type doesn't have items");
        }

        return new ArraySchema(parse(itemsNode, names, encSpace), propMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ArraySchema) {
            ArraySchema that = (ArraySchema) obj;
            if (itemSchema.equals(that.itemSchema)) {
                return ObjectUtil.equals(getPropertyMap(), that.getPropertyMap());
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 29 * itemSchema.hashCode() + ObjectUtil.hashCode(getPropertyMap());
    }
}
