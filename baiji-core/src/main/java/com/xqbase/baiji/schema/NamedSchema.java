package com.xqbase.baiji.schema;

import com.xqbase.baiji.common.util.StringUtils;
import com.xqbase.baiji.exceptions.BaijiRuntimeException;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
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
    private Set<String> aliases;

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
                Set<String> aliases, PropertyMap props, SchemaNames names) {
        super(type, props);
        this.schemaName = schemaName;
        this.doc = doc;
        this.aliases = aliases;
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
        Set<String> aliases = getAliases(node);

        NamedSchema result = null;
        if ("enum".equals(type)) {
            result = EnumSchema.newInstance(node, name, doc, aliases, props, names);
        } else if ("record".equals(type)) {
            result = RecordSchema.newInstance(node, name, doc, aliases, props, names);
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
     */
    protected static Set<String> getAliases(JsonNode schema) {
        JsonNode aliasesNode = schema.get("aliases");
        if (null == aliasesNode) {
            return null;
        }

        if (!aliasesNode.isArray()) {
            throw new SchemaParseException("Aliases must be of format JSON array of strings");
        }

        Set<String> aliases = new LinkedHashSet<>();
        for (JsonNode aliasNode : aliasesNode) {
            if (!aliasNode.isTextual()) {
                throw new SchemaParseException("Aliases must be of format JSON array of strings");
            }

            aliases.add(aliasNode.getTextValue());
        }
        return aliases;
    }

    @Override
    protected void writeJSON(JsonGenerator gen, SchemaNames names) throws IOException {
        if (!names.add(this)) {
            gen.writeString(getSchemaName().getName());
        } else {
            super.writeJSON(gen, names);
        }
    }

    @Override
    protected void writeJsonFields(JsonGenerator gen, SchemaNames names) throws IOException {
        schemaName.writeJSON(gen);

        if (StringUtils.hasLength(doc)) {
            gen.writeFieldName("doc");
            gen.writeString(doc);
        }

        if (aliases != null) {
            gen.writeFieldName("aliases");
            gen.writeStartArray();
            for (String a : aliases) {
                gen.writeString(a);
            }
            gen.writeEndArray();
        }
    }
}
