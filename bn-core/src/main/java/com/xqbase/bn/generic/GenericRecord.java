package com.xqbase.bn.generic;

/**
 * A generic instance of a record schema. Fields are accessible
 * by name as well as by index.
 *
 * @author Tony He
 */
public interface GenericRecord extends IndexedRecord {

    /**
     * Set the value of a field given its name.
     */
    void put(String key, Object v);

    /**
     * Return the value of a field given its name.
     */
    Object get(String key);
}
