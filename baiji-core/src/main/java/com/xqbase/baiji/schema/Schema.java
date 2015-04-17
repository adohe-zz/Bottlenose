package com.xqbase.baiji.schema;


import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;

/**
 * An abstract data type.
 * <p>A schema may be one of:
 * <ul>
 * <li>A <i>record</i>, mapping field names to field value data;
 * <li>An <i>enum</i>, containing one of a small set of symbols;
 * <li>An <i>array</i> of values, all of the same schema;
 * <li>A <i>map</i>, containing string/value pairs, of a declared schema;
 * <li>A <i>union</i> of other schemas;
 * <li>A unicode <i>string</i>;
 * <li>A sequence of <i>bytes</i>;
 * <li>A 32-bit signed <i>int</i>;
 * <li>A 64-bit signed <i>long</i>;
 * <li>A 32-bit IEEE single-<i>float</i>; or
 * <li>A 64-bit IEEE <i>double</i>-float; or
 * <li>A <i>boolean</i>; or
 * <li><i>null</i>.
 * </ul>
 * <p/>
 * The schema objects are <i>logically</i> immutable.
 *
 * @author Tony He
 */
public abstract class Schema {

    private static final JsonFactory FACTORY = new JsonFactory();
    private static final ObjectMapper MAPPER = new ObjectMapper(FACTORY);

    static {
        FACTORY.enable(JsonParser.Feature.ALLOW_COMMENTS);
        FACTORY.setCodec(MAPPER);
    }

    private final SchemaType type;
    private final PropertyMap propertyMap;

    protected Schema(SchemaType type, PropertyMap propertyMap) {
        this.type = type;
        this.propertyMap = propertyMap;
    }

    /**
     * Get the type of the schema.
     * @return type of schema
     */
    public SchemaType getType() {
        return type;
    }

    /**
     * Get additional JSON attributes apart from those defined in the Baiji spec.
     * @return additional JSON attributes
     */
    public PropertyMap getPropertyMap() {
        return propertyMap;
    }

    /**
     * The name of this schema. If this is a named schema such as an enum,
     * it returns the fully qualified name for the schema.
     * For other schemas, it returns the type of the schema.
     *
     * @return the qualified or type of the schema.
     */
    public abstract String getName();

    /**
     * Parses a JSON string to create a new schema object.
     *
     * @param json JSON string
     * @return a new Schema Object
     */
    public static Schema parse(String json) {
        if (json == null || json.isEmpty()) {
            throw new IllegalArgumentException("JSON string cannot be null or empty.");
        }

        return parse(json.trim(), new SchemaNames(), null);
    }

    /**
     * Parses a JSON string to create a new schema object.
     *
     * @param json JSON string to parse.
     * @param names list of {@link com.xqbase.baiji.schema.SchemaName}s already read.
     * @param encSpace enclosing namespace of the schema
     * @return a schema object.
     */
    private static Schema parse(String json, SchemaNames names, String encSpace) {
        return null;
    }
}
