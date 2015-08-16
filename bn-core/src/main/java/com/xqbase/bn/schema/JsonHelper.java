package com.xqbase.bn.schema;

import com.xqbase.bn.common.util.StringUtils;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;

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

    /** Extracts text value associated to key from the container JsonNode,
     * and throws {@link SchemaParseException} if it doesn't exist.
     *
     * @param container Container where to find key.
     * @param key Key to look for in container.
     * @param error String to prepend to the SchemaParseException.
     */
    public static String getRequiredString(JsonNode container, String key, String error) {
        String value = getOptionalString(container, key);
        if (!StringUtils.hasLength(value)) {
            throw new SchemaParseException(error + ":" + container);
        }
        return value;
    }

    /**
     * Returns value of specific string field (this field is optional).
     *
     * @param container the JSON Object node.
     * @param key the key.
     * @return the value of field otherwise throw {@link SchemaParseException}
     */
    public static String getOptionalString(JsonNode container, String key) {
        if (null == container) {
            throw new IllegalArgumentException("node cannot be null.");
        }
        ensureValidKey(key);

        JsonNode jsonNode = container.get(key);
        return jsonNode != null ? jsonNode.getTextValue() : null;
    }

    private static void ensureValidKey(String key) {
        if (!StringUtils.hasLength(key)) {
            throw new IllegalArgumentException("key cannot be null or empty");
        }
    }

    public static void writeIfNotNullOrEmpty(JsonGenerator gen, String key, String value)
            throws IOException {
        if (!StringUtils.hasLength(value)) {
            return;
        }
        gen.writeStringField(key, value);
    }
}
