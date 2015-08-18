package com.xqbase.bn.schema;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A collection of NamedSchema mapping.
 *
 * @author Tony He
 */
public class SchemaNames implements Iterable<Map.Entry<SchemaName, NamedSchema>> {

    private final Map<SchemaName, NamedSchema> names = new HashMap<>();

    public Map<SchemaName, NamedSchema> getNames() {
        return names;
    }

    public boolean contains(SchemaName name) {
        return names.containsKey(name);
    }

    public boolean add(SchemaName name, NamedSchema schema) {
        if (names.containsKey(name)) {
            return false;
        }
        names.put(name, schema);
        return true;
    }

    public boolean add(NamedSchema schema) {
        return add(schema.getSchemaName(), schema);
    }

    public NamedSchema getSchema(String name, String space) {
        return names.get(new SchemaName(name, space));
    }

    @Override
    public Iterator<Map.Entry<SchemaName, NamedSchema>> iterator() {
        return names.entrySet().iterator();
    }
}
