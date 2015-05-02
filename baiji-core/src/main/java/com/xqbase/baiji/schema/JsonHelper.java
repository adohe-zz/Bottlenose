package com.xqbase.baiji.schema;

import org.codehaus.jackson.JsonNode;

/**
 * JSON Utility.
 *
 * @author Tony He
 */
public class JsonHelper {

    private JsonHelper() {
    }

    /**
     * Static function to parse custom properties (not defined in the Baiji spec) from the given JSON object
     *
     * @param node JSON object to parse
     * @return Property map if custom properties were found, null if no custom properties found
     */
    public static PropertyMap getProperties(JsonNode node) {
        PropertyMap props = new PropertyMap();
        props.parse(node);
        return !props.isEmpty() ? props : null;
    }

    /**
     * Returns value of specific string field (this field is required).
     *
     * @param node the JSON Object node.
     * @param field field name.
     * @return the value of field otherwise throw {@link SchemaParseException}
     */
    public static String getRequiredString(JsonNode node, String field) {
        String value = getOptionalString(node, field);
        if (value == null || value.length() == 0) {
            throw new SchemaParseException("No \"" + field + "\" JSON field: " + node);
        }
        return value;
    }

    /**
     * Returns value of specific string field (this field is optional).
     *
     * @param node the JSON Object node.
     * @param field field name.
     * @return the value of field otherwise throw {@link SchemaParseException}
     */
    public static String getOptionalString(JsonNode node, String field) {
        if (node == null) {
            throw new IllegalArgumentException("node cannot be null.");
        }
        ensureValidFieldName(field);

        JsonNode fieldNode = node.get(field);
        if (fieldNode == null) {
            return null;
        }

        if (fieldNode.isNull()) {
            return null;
        }
        if (fieldNode.isTextual()) {
            return fieldNode.getTextValue();
        }
        throw new SchemaParseException("Field " + field + " is not a string");
    }

    private static void ensureValidFieldName(String field) {
        if (field == null || field.isEmpty()) {
            throw new IllegalArgumentException("field cannot be null or empty");
        }
    }
}
