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

}
