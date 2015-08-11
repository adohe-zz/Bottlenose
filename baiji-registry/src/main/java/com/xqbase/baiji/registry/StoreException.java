package com.xqbase.baiji.registry;

/**
 * KV Store exception.
 *
 * @author Tony He
 */
public class StoreException extends Exception {

    public StoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public StoreException(String message) {
        super(message);
    }
}
