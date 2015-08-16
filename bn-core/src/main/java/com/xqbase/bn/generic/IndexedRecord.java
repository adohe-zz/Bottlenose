package com.xqbase.bn.generic;

/**
 * A record implementation that permits field access by integer index.
 *
 * @author Tony He
 */
public interface IndexedRecord extends GenericContainer {

    /**
     * Set the value of a field given its position in the schema.
     * <p>This method is not meant to called by user code, but only
     * by {@link com.xqbase.bn.io.DatumReader} implementations.</p>
     */
    void put(int i, Object v);

    /**
     * Return the value of a field given its position in the schema.
     * <p>This method is not meant to called by user code, but only
     * by {@link com.xqbase.bn.io.DatumWriter} implementations.</p>
     */
    Object get(int i);
}
