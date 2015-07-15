package com.xqbase.baiji.common.logging.slf4j;

import com.xqbase.baiji.common.logging.ILoggerFactory;
import com.xqbase.baiji.common.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 * Slf4j Logger Factory.
 *
 * @author Tony He
 */
public class Slf4jLoggerFactory implements ILoggerFactory {

    public static boolean available() {
        try {
            return Class.forName("org.slf4j.LoggerFactory") != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String name() {
        return "slf4j";
    }

    @Override
    public Logger getLogger(Class clazz) {
        return new Slf4jLogger(LoggerFactory.getLogger(clazz));
    }

    @Override
    public Logger getLogger(String name) {
        return new Slf4jLogger(LoggerFactory.getLogger(name));
    }
}
