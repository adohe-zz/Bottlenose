package com.xqbase.baiji.generic;

/**
 * Utilities for generic Java data.
 *
 * @author Tony He
 */
public class GenericData {

    private static final GenericData INSTANCE = new GenericData();

    private final ClassLoader classLoader;

    /** Return the singleton instance. */
    public static GenericData get() {
        return INSTANCE;
    }

    /** For subclass. Applications normally use {@link GenericData#get()}. */
    public GenericData() {
        this(null);
    }

    /** For subclass. Applications normally use {@link GenericData#get()}. */
    public GenericData(ClassLoader classLoader) {
        this.classLoader = (classLoader != null)
                ? classLoader
                : getClass().getClassLoader();
    }

    public Object getField(Object record, String name, int position) {
        return ((IndexedRecord) record).get(position);
    }
}
