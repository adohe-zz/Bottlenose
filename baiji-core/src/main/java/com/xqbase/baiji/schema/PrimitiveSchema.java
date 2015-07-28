package com.xqbase.baiji.schema;

import com.xqbase.baiji.util.ObjectUtil;
import org.codehaus.jackson.JsonGenerator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * This schema is used to represent the primitive type.
 *
 * @author Tony He
 */
public class PrimitiveSchema extends UnnamedSchema {

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
        SchemaType schemaType = PRIMITIVES.get(type);
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

    @Override
    protected void writeJSON(JsonGenerator gen, SchemaNames names) throws IOException {
        gen.writeString(getName());
    }
}
