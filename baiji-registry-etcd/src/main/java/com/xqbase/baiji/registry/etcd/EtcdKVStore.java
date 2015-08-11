package com.xqbase.baiji.registry.etcd;

import com.xqbase.baiji.registry.KVStore;
import com.xqbase.baiji.registry.Node;
import com.xqbase.baiji.registry.StoreException;

/**
 * ETCD backend KV store implementation.
 *
 * @author Tony He
 */
public class EtcdKVStore implements KVStore {

    @Override
    public Node list(String key, boolean recursive) throws StoreException {
        return null;
    }

    @Override
    public String get(String key) throws StoreException {
        return null;
    }

    @Override
    public void set(String key, String value, Integer ttl) throws StoreException {

    }

    @Override
    public void delete(String key) throws StoreException {

    }

    @Override
    public void deleteDir(String key, boolean recursive) throws StoreException {

    }

    @Override
    public void createDir(String key) throws StoreException {

    }
}
