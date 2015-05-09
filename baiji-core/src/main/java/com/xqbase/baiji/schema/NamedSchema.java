package com.xqbase.baiji.schema;

import com.xqbase.baiji.exceptions.BaijiRuntimeException;
import org.codehaus.jackson.JsonNode;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * The abstract named schema.
 *
 * @author Tony He
 */
public abstract class NamedSchema extends Schema {

    private final SchemaName schemaName;
    private final String doc;
    private Set<SchemaName> aliases;

    /**
     * Construct a named schema.
     *
     * @param type the schema type.
     * @param schemaName the schema name.
     * @param doc the schema doc.
     * @param props the schema properties map.
     * @param names list of named schemas already read
     */
    protected NamedSchema(SchemaType type, SchemaName schemaName, String doc,
                Set<SchemaName> aliases, PropertyMap props, SchemaNames names) {
        super(type, props);
        this.schemaName = schemaName;
        this.doc = doc;
        if (schemaName.getName() != null && !names.add(schemaName, this)) {
            throw new BaijiRuntimeException("Duplicated schema name " + schemaName.getFullName());
        }
    }

    // Static newInstance method.
    protected static NamedSchema newInstance(JsonNode node, PropertyMap props, SchemaNames names) {
        String type = JsonHelper.getRequiredString(node, "type", "No type");
        String space = JsonHelper.getOptionalString(node, "namespace");
        String doc = JsonHelper.getOptionalString(node, "doc");
        SchemaName name = new SchemaName(JsonHelper.getRequiredString(node, "name", "No schema name"), space);

        NamedSchema result;
        if ("enum".equals(type)) {
            result = EnumSchema.newInstance(node, name, doc, props, names);
        } else if ("record".equals(type)) {
            result = RecordSchema.newInstance(node, name, doc, props, names);
        } else {
            return names.getSchema(type, null);
        }

        return result;
    }

    public SchemaName getSchemaName() {
        return schemaName;
    }

    @Override
    public String getName() {
        return schemaName.getName();
    }

    public String getNameSpace() {
        return schemaName.getNameSpace();
    }

    public String getFullName() {
        return schemaName.getFullName();
    }

    public String getDoc() {
        return doc;
    }

    /**
     * Parses the 'aliases' property from the given JSON token. (just named schema may has such property)
     *
     * @param schema     JSON object to read
     * @param space      namespace of the name this alias is for
     * @return Set of SchemaName that represents the list of alias. If no 'aliases' specified, then it returns null.
     */
    protected static Set<SchemaName> getAliases(JsonNode schema, String space) {
        JsonNode aliasesNode = schema.get("aliases");
        if (null == aliasesNode) {
            return null;
        }

        if (!aliasesNode.isArray()) {
            throw new SchemaParseException("Aliases must be of format JSON array of strings");
        }

        Set<SchemaName> aliases = new LinkedHashSet<>();
        for (JsonNode aliasNode : aliasesNode) {
            if (!aliasNode.isTextual()) {
                throw new SchemaParseException("Aliases must be of format JSON array of strings");
            }

            aliases.add(new SchemaName(aliasNode.getTextValue(), space));
        }
        return aliases;
    }
}
