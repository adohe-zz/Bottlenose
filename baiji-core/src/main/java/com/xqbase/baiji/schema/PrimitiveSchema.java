package com.xqbase.baiji.schema;

import com.xqbase.baiji.util.ObjectUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * This schema is used to represent the primitive type.
 *
 * @author Tony He
 */
public class PrimitiveSchema extends UnnamedSchema {

    private static final Map<String, SchemaType> typeMap = new HashMap<String, SchemaType>();

    static {
        typeMap.put("null", SchemaType.NULL);
        typeMap.put("boolean", SchemaType.BOOLEAN);
        typeMap.put("int", SchemaType.INT);
        typeMap.put("long", SchemaType.LONG);
        typeMap.put("float", SchemaType.FLOAT);
        typeMap.put("double", SchemaType.DOUBLE);
        typeMap.put("bytes", SchemaType.BYTES);
        typeMap.put("string", SchemaType.STRING);
        typeMap.put("datetime", SchemaType.DATETIME);
    }

    protected PrimitiveSchema(SchemaType type, PropertyMap propertyMap) {
        super(type, propertyMap);
    }

    /**
     * Create a schema for a primitive type.
     */
    public static PrimitiveSchema newInstance(String type) {
        return newInstance(type, null);
    }

    /**
     * Create a schema for a primitive type.
     */
    public static PrimitiveSchema newInstance(String type, PropertyMap props) {
        final String quote = "\"";
        if (type.startsWith(quote) && type.endsWith(quote)) {
            type = type.substring(1, type.length() - 2);
        }
        SchemaType schemaType = typeMap.get(type);
        return schemaType != null ? new PrimitiveSchema(schemaType, props) : null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PrimitiveSchema)) {
            return false;
        }
        PrimitiveSchema that = (PrimitiveSchema) obj;
        return getType() == that.getType() && ObjectUtil.equals(that.getPropertyMap(), getPropertyMap());
    }

    @Override
    public int hashCode() {
        return 13 * getType().hashCode() + ObjectUtil.hashCode(getPropertyMap());
    }
}
