package com.xqbase.baiji.schema;

import com.xqbase.baiji.exceptions.BaijiRuntimeException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ObjectNode;

import java.util.*;

/**
 * The properties map of each schema.
 *
 * @author Tony He
 */
public class PropertyMap extends HashMap<String, String> {

    private static final Set<String> reserved = new HashSet<String>();

    static {
        reserved.add("type");
        reserved.add("name");
        reserved.add("namespace");
        reserved.add("fields");
        reserved.add("items");
        reserved.add("size");
        reserved.add("symbols");
        reserved.add("values");
        reserved.add("aliases");
        reserved.add("order");
        reserved.add("doc");
        reserved.add("default");
    }

    @Override
    public synchronized String put(String key, String value) {
        if (reserved.contains(key)) {
            throw new BaijiRuntimeException("Can't set reserved property: " + key);
        }

        String oldValue = get(key);
        if (oldValue == null) {
            return super.put(key, value);
        } else if (!oldValue.equals(value)) {
            throw new BaijiRuntimeException("Property can't be overwritten: " + key);
        }
        return value;
    }

    public void parse(JsonNode node) {
        if (!(node instanceof ObjectNode))
            return;

        ObjectNode objNode = (ObjectNode) node;
        Iterator<Map.Entry<String, JsonNode>> fields = objNode.getFields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            if (reserved.contains(key))
                continue;
            JsonNode value = entry.getValue();
            if (!containsKey(key)) {
                put(key, value.isTextual() ? value.getTextValue() : value.asText());
            }
        }
    }
}
