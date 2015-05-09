package com.xqbase.baiji.schema;

import com.xqbase.baiji.util.ObjectUtil;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The UnionSchema definition.
 *
 * @author Tony He
 */
public class UnionSchema extends UnnamedSchema {

    private final List<Schema> schemas;

    protected UnionSchema(final List<Schema> schemas, PropertyMap propertyMap) {
        super(SchemaType.UNION, propertyMap);
        if (schemas == null) {
            throw new IllegalArgumentException("schema list should not be null");
        }
        this.schemas = schemas;
    }

    /**
     * Static function to return instance of the union schema
     *
     * @param array    JSON object for the union schema
     * @param props    properties map.
     * @param names    list of named schemas already read
     * @return a new {@link UnionSchema} instance
     */
    public static UnionSchema newInstance(ArrayNode array, PropertyMap props, SchemaNames names) {
        List<Schema> schemas = new ArrayList<Schema>();
        Map<String, String> uniqueSchemas = new HashMap<String, String>();

        for (JsonNode node : array) {
            Schema unionType = parse(node, names);
            if (null == unionType) {
                throw new SchemaParseException("Invalid JSON in union" + node);
            }

            String name = unionType.getName();
            if (uniqueSchemas.containsKey(name)) {
                throw new SchemaParseException("Duplicate type in union: " + name);
            }

            uniqueSchemas.put(name, name);
            schemas.add(unionType);
        }

        return new UnionSchema(schemas, props);
    }

    /**
     * @return List of schemas in the union
     */
    public List<Schema> getSchemas() {
        return schemas;
    }

    /**
     * @return Count of schemas in the union
     */
    public int size() {
        return schemas.size();
    }

    /**
     * Returns the schema at the given branch.
     *
     * @param index Index to the branch, starting with 0.
     * @return The branch corresponding to the given index.
     */
    public Schema get(int index) {
        return schemas.get(index);

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UnionSchema)) {
            return false;
        }
        UnionSchema that = (UnionSchema) obj;
        if (!that.schemas.equals(schemas)) {
            return false;
        }
        return ObjectUtil.equals(that.getPropertyMap(), getPropertyMap());
    }


    @Override
    public int hashCode() {
        int result = 53;
        for (Schema schema : schemas) {
            result += 89 * schema.hashCode();
        }
        result += ObjectUtil.hashCode(getPropertyMap());
        return result;
    }
}
