package com.xqbase.baiji.schema;

import java.util.List;

/**
 * The EnumSchema Definiton.
 *
 * @author Tony He
 */
public class EnumSchema extends NamedSchema {

    /**
     * Construct a named schema.
     *
     * @param type       the schema type.
     * @param schemaName the schema name.
     * @param doc        the schema doc.
     * @param aliases    schema aliases list.
     * @param props      the schema properties map.
     * @param names      list of named schemas already read
     */
    protected EnumSchema(SchemaType type, SchemaName schemaName, String doc, List<SchemaName> aliases, PropertyMap props, SchemaNames names) {
        super(type, schemaName, doc, aliases, props, names);
    }
}
