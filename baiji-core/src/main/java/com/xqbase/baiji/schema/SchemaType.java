package com.xqbase.baiji.schema;

/**
 * This Enum represents the schema type.
 *
 * @author Tony He
 */
public enum SchemaType {

    RECORD, ENUM, ARRAY, MAP, UNION, DATETIME, STRING, BYTES,
    INT, LONG, FLOAT, DOUBLE, BOOLEAN, NULL;

    private String name;

    private SchemaType() {
        this.name = this.name().toLowerCase();
    }

    public String getName() {
        return name;
    }
}
