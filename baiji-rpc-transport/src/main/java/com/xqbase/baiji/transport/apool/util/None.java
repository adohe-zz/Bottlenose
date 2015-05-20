package com.xqbase.baiji.transport.apool.util;

/**
 * Object that represents no value.
 *
 * @author Tony He
 */
public class None {

    private static final None INSTANCE = new None();

    private None() {

    }

    public static None none() {
        return INSTANCE;
    }
}