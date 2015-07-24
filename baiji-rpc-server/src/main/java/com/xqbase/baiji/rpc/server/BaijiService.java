package com.xqbase.baiji.rpc.server;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Classes that can be used to bootstrap and launch a Baiji service
 * from a Java main method.
 *
 * @author Tony He
 */
public class BaijiService {

    private Class<?> mainServiceClass;
    private Set<Object> sources = new LinkedHashSet<>();

    public static BaijiService service(Object source) {
        return new BaijiService(source);
    }

    private BaijiService(Object... sources) {

    }

    private void run(String... args) {

    }

    public static void run(Object source, String... args) {

    }

    public static void run(Object[] sources, String... args) {

    }
}
