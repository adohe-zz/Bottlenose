package com.xqbase.bn.schema;

import com.xqbase.bn.exceptions.BaijiRuntimeException;
import com.xqbase.bn.util.ObjectUtil;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;

import java.io.IOException;
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
    private final Map<String,Integer> indexByName
            = new HashMap<>();

    protected UnionSchema(final LockableArrayList<Schema> schemas, PropertyMap propertyMap) {
        super(SchemaType.UNION, propertyMap);
        this.schemas = schemas.lock();
        int index = 0;
        for (Schema schema : schemas) {
            if (schema.getType() == SchemaType.UNION)
                throw new BaijiRuntimeException("Nested union" + this);
            String name = schema.getName();
            if (null == name)
                throw new BaijiRuntimeException("Nameless in union" + this);
            if (indexByName.put(name, index ++) != null)
                throw new BaijiRuntimeException("Duplicate in union" + name);
        }
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
        LockableArrayList<Schema> schemas = new LockableArrayList<>(array.size());

        for (JsonNode node : array) {
            Schema unionType = parse(node, names);
            if (null == unionType) {
                throw new SchemaParseException("Invalid JSON in union" + node);
            }

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
    protected void writeJSON(JsonGenerator gen, SchemaNames names) throws IOException {
        gen.writeStartArray();
        for (Schema schema : schemas) {
            schema.writeJSON(gen, names);
        }
        gen.writeEndArray();
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
        return schemas.equals(that.schemas)
                    && ObjectUtil.equals(that.getPropertyMap(), getPropertyMap());
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
