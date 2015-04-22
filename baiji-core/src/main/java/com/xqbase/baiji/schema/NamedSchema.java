package com.xqbase.baiji.schema;

import com.xqbase.baiji.exceptions.BaijiRuntimeException;
import org.codehaus.jackson.JsonNode;

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

    /**
     * Construct a named schema.
     *
     * @param type the schema type.
     * @param schemaName the schema name.
     * @param doc the schema doc.
     * @param aliases schema aliases list.
     * @param props the schema properties map.
     * @param names list of named schemas already read
     */
    protected NamedSchema(SchemaType type, SchemaName schemaName, String doc, List<SchemaName> aliases,
                          PropertyMap props, SchemaNames names) {
        super(type, props);
        this.schemaName = schemaName;
        this.doc = doc;
        this.aliases = aliases;
        if (schemaName.getName() != null && !names.add(schemaName, this)) {
            throw new BaijiRuntimeException("Duplicated schema name " + schemaName.getFullName());
        }
    }

    // Static newInstance method.
    public static NamedSchema newInstance(JsonNode node, PropertyMap props, SchemaNames names,
                                   String encSpace) {
        String type = JsonHelper.getRequiredString(node, "type");
        if ("enum".equals(type)) {
            return EnumSchema.newInstance(node, props, names, encSpace);
        } else if ("record".equals(type)) {
            return RecordSchema.newInstance(node, props, names, encSpace);
        } else {
            return names.getSchema(type, null, encSpace);
        }
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
