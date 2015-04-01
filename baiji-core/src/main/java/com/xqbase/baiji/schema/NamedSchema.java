package com.xqbase.baiji.schema;

import java.util.List;

/**
 * The abstract named schema.
 *
 * @author Tony He
 */
public abstract class NamedSchema extends Schema {

    protected NamedSchema(SchemaType type, SchemaName schemaName, String doc, List<SchemaName> aliases,
                          PropertyMap props, SchemaNames names) {
        super(type, props);
    }
}
