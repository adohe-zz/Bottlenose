package com.xqbase.baiji.schema;

import org.codehaus.jackson.JsonNode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The RecordSchema definition.
 *
 * @author Tony He
 */
public class RecordSchema extends NamedSchema implements Iterable<Field> {

    private final List<Field> fields;

    private final Map<String, Field> fieldLookup;

    private final Map<String, Field> fieldAliasLookup;

    /**
     * Construct a named schema.
     *
     * @param schemaName the schema name.
     * @param doc        the schema doc.
     * @param aliases    schema aliases list.
     * @param props      the schema properties map.
     */
    protected RecordSchema(SchemaName schemaName, String doc, List<SchemaName> aliases, PropertyMap props,
                    List<Field> fields) {
        super(SchemaType.RECORD, schemaName, doc, aliases, props, new SchemaNames());
        this.fields = fields;
        Map<String, Field> fieldMap = new HashMap<>();
        Map<String, Field> fieldAliasMap = new HashMap<>();
        for (Field field : fields) {
            addToFieldMap(fieldMap, field.getName(), field);
            addToFieldMap(fieldAliasMap, field.getName(), field);

            if (field.getAliases() != null) {
                for (String alias : field.getAliases()) {
                    addToFieldMap(fieldAliasMap, alias, field);
                }
            }
        }

        fieldLookup = fieldMap;
        fieldAliasLookup = fieldAliasMap;
    }

    /**
     * Constructor for the record schema
     *
     * @param name          name of the record schema
     * @param doc           the schema doc
     * @param aliases       list of aliases for the record name
     * @param props         the schema properties map
     * @param fields        list of fields for the record
     * @param request       true if this is an anonymous record with 'request' instead of 'fields'
     * @param fieldMap      map of field names and field objects
     * @param fieldAliasMap map of field aliases and field objects
     * @param names         list of named schema already read
     */
    private RecordSchema(SchemaName name, String doc, List<SchemaName> aliases, PropertyMap props,
                         List<Field> fields, boolean request, Map<String, Field> fieldMap,
                         Map<String, Field> fieldAliasMap, SchemaNames names) {
        super(SchemaType.RECORD, name, doc, aliases, props, names);
        if (!request && name.getName() == null) {
            throw new SchemaParseException("name cannot be null for record schema.");
        }
        this.fields = fields;
        this.fieldLookup = fieldMap;
        this.fieldAliasLookup = fieldAliasMap;
    }

    /**
     * Static function to return new instance of the record schema
     *
     * @param node     JSON object for the record schema
     * @param name     name of the record schema
     * @param doc      documentation to the user of this schema
     * @param props    the schema properties map
     * @param names    list of named schema already read
     * @return a new {@link RecordSchema} instance
     */
    protected static RecordSchema newInstance(JsonNode node, SchemaName name, String doc, PropertyMap props,
                SchemaNames names) {
        JsonNode fieldsNode = node.get("fields");
        if (null == fieldsNode || !fieldsNode.isArray()) {
            throw new SchemaParseException("Record has no fields " + node);
        }
        for (JsonNode field : fieldsNode) {
            String fieldName = JsonHelper.getRequiredString(field, "name", "No field name");
            String fieldDoc = JsonHelper.getOptionalString(field, "doc");
            JsonNode fieldTypeNode = field.get("type");
            if (null == fieldTypeNode) {
                throw new SchemaParseException("No field type " + field);
            }
            if (fieldTypeNode.isTextual()) {

            }

        }

        return null;
    }

    // Add one field to the field map
    private static void addToFieldMap(Map<String, Field> map, String name, Field field) {
        String lowerCaseName = name.toLowerCase();
        if (map.containsKey(lowerCaseName)) {
            throw new SchemaParseException("field or alias " + name + " is a duplicate name");
        }
        map.put(lowerCaseName, field);
    }

    @Override
    public Iterator<Field> iterator() {
        return null;
    }
}
