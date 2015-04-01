package com.xqbase.baiji.schema;

import java.util.List;

/**
 * The abstract named schema.
 *
 * @author Tony He
 */
public abstract class NamedSchema extends Schema {

    private final SchemaName schemaName;
    private final String doc;
    private final List<SchemaName> aliases;

    protected NamedSchema(SchemaType type, SchemaName schemaName, String doc, List<SchemaName> aliases,
                          PropertyMap props, SchemaNames names) {
        super(type, props);
        this.schemaName = schemaName;
        this.doc = doc;
        this.aliases = aliases;
    }

    public SchemaName getSchemaName() {
        return schemaName;
    }

    @Override
    public String getName() {
        return schemaName.getName();
    }

    public String getNamespace() {
        return schemaName.getNamespace();
    }

    public String getFullName() {
        return schemaName.getFullName();
    }

    public String getDoc() {
        return doc;
    }
}
