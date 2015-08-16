package com.xqbase.bn.schema;

/**
 * The abstract unnamed schema.
 *
 * @author Tony He
 */
public abstract class UnnamedSchema extends Schema {

    protected UnnamedSchema(SchemaType type, PropertyMap propertyMap) {
        super(type, propertyMap);
    }

    @Override
    public String getName() {
        return getType().getName().toLowerCase();
    }
}
