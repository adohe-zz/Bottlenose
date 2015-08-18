package com.xqbase.bn.schema;

import com.xqbase.bn.common.util.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.DoubleNode;

import java.util.*;

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
     * @param aliases    schema aliases set.
     * @param props      the schema properties map.
     */
    protected RecordSchema(SchemaName schemaName, String doc, Set<String> aliases, PropertyMap props,
                    List<Field> fields, Map<String, Field> fieldMap,
                    Map<String, Field> fieldAliasMap, SchemaNames names) {
        super(SchemaType.RECORD, schemaName, doc, aliases, props, names);
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
     * @param aliases  schema aliases set
     * @param props    the schema properties map
     * @param names    list of named schema already read
     * @return a new {@link RecordSchema} instance
     */
    protected static RecordSchema newInstance(JsonNode node, SchemaName name, String doc,
                Set<String> aliases, PropertyMap props, SchemaNames names) {
        JsonNode fieldsNode = node.get("fields");
        if (null == fieldsNode || !fieldsNode.isArray()) {
            throw new SchemaParseException("Record has no fields " + node);
        }
        List<Field> fieldList = new ArrayList<>();
        Map<String, Field> fieldMap = new HashMap<>();
        Map<String, Field> fieldAliasMap = new HashMap<>();
        RecordSchema result = new RecordSchema(name, doc, aliases, props, fieldList, fieldMap,
                fieldAliasMap, names);

        int pos = 1;
        for (JsonNode field : fieldsNode) {
            String fieldName = JsonHelper.getRequiredString(field, "name", "Field node has no name field");
            Field f = createFiled(field, pos++, names);

            fieldList.add(f);
            addToFieldMap(fieldMap, fieldName, f);
            addToFieldMap(fieldAliasMap, fieldName, f);
            if (f.getAliases() != null && f.getAliases().size() > 0) {
                for (String alias : f.getAliases()) {
                    addToFieldMap(fieldAliasMap, alias, f);
                }
            }
        }

        return result;
    }

    // Add one field to the field map
    private static void addToFieldMap(Map<String, Field> map, String name, Field field) {
        String lowerCaseName = name.toLowerCase();
        if (map.containsKey(lowerCaseName)) {
            throw new SchemaParseException("field or alias " + name + " is a duplicate name");
        }
        map.put(lowerCaseName, field);
    }

    /**
     * Create Record field.
     */
    private static Field createFiled(JsonNode field, int pos, SchemaNames names) {
        String fieldName = JsonHelper.getRequiredString(field, "name", "No field name");
        String fieldDoc = JsonHelper.getOptionalString(field, "doc");
        JsonNode fieldTypeNode = field.get("type");
        if (null == fieldTypeNode) {
            throw new SchemaParseException("No field type " + field);
        }

        Schema fieldSchema = parse(fieldTypeNode, names);
        Field.SortOrder order = Field.SortOrder.ASCENDING;
        JsonNode orderNode = field.get("order");
        if (orderNode != null)
            order = Field.SortOrder.valueOf(orderNode.getTextValue().toUpperCase());
        JsonNode defaultValue = field.get("default");
        if (defaultValue != null
                && (SchemaType.FLOAT.equals(fieldSchema.getType())
                || (SchemaType.DOUBLE.equals(fieldSchema.getType())))
                && defaultValue.isTextual()) {
            defaultValue = new DoubleNode(Double.valueOf(defaultValue.getTextValue()));
        }
        Set<String> fieldAliases = getAliases(field);
        PropertyMap props = JsonHelper.getProperties(field);
        return new Field(fieldSchema, fieldName, fieldAliases, pos,
                fieldDoc, defaultValue, order, props);
    }

    /**
     * Get fields list.
     */
    public List<Field> getFields() {
        return fields;
    }

    public int getFieldsSize() {
        return fields.size();
    }

    /**
     * Returns the field with the given name.
     *
     * @param name field name
     * @return Field object
     */
    public Field getField(String name) {
        if (!StringUtils.hasLength(name)) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return fieldLookup.get(name.toLowerCase());
    }

    public Field getFieldByAlias(String alias) {
        if (!StringUtils.hasLength(alias)) {
            throw new IllegalArgumentException("name cannot be null");
        }
        return fieldAliasLookup.get(alias.toLowerCase());
    }

    @Override
    public Iterator<Field> iterator() {
        return fields.iterator();
    }
}
