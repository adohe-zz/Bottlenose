package com.xqbase.baiji.schema;

import com.xqbase.baiji.exceptions.BaijiTypeException;
import com.xqbase.baiji.util.ObjectUtil;
import org.codehaus.jackson.JsonNode;

/**
 * The MapSchema Definition.
 *
 * @author Tony He
 */
public class MapSchema extends UnnamedSchema {

    private final Schema valueSchema;

    protected MapSchema(Schema valueSchema, PropertyMap propertyMap) {
        super(SchemaType.ARRAY, propertyMap);
        if (valueSchema == null)
            throw new IllegalArgumentException("Map value schema can't be null");
        this.valueSchema = valueSchema;
    }

    /**
     * Static function to return new instance of map schema
     *
     * @param mapNode  JSON object for the map schema.
     * @param props    properties map.
     * @param names    list of named schemas already read.
     * @param encSpace enclosing namespace of the map schema.
     * @return a {@link MapSchema} instance.
     */
    public static MapSchema newInstance(JsonNode mapNode, PropertyMap props, SchemaNames names, String encSpace) {
        JsonNode valuesNode = mapNode.get("values");
        if (valuesNode == null) {
            throw new BaijiTypeException("Map does not have 'values'");
        }

        return new MapSchema(parse(valuesNode, names, encSpace), props);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MapSchema)) {
            return false;
        }
        MapSchema that = (MapSchema) obj;
        if (that == null) {
            return false;
        }
        return valueSchema.equals(that.valueSchema) && ObjectUtil.equals(that.getPropertyMap(), getPropertyMap());
    }


    @Override
    public int hashCode() {
        return 29 * valueSchema.hashCode() + ObjectUtil.hashCode(getPropertyMap());
    }
}