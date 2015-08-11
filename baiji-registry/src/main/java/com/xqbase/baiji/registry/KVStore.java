package com.xqbase.baiji.registry;

/**
 * The KVStore interface.
 *
 * @author Tony He
 */
public interface KVStore {

    /**
     * List Node under key.
     * @throws StoreException
     */
    Node list(String key, boolean recursive) throws StoreException;

    /**
     * Get the value of a key.
     * @throws StoreException
     */
    String get(String key) throws StoreException;

    /**
     * Set the value and ttl of a key.
     * @throws StoreException
     */
    void set(String key, String value, Integer ttl) throws StoreException;

    /**
     * Delete a key.
     * @throws StoreException
     */
    void delete(String key) throws StoreException;

    /**
     * Delete a dir
     * @throws StoreException
     */
    void deleteDir(String key, boolean recursive) throws StoreException;

    /**
     * Create a dir
     * @throws StoreException
     */
    void createDir (String key) throws StoreException;
}
