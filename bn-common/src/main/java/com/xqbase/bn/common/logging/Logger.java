package com.xqbase.bn.common.logging;

import java.util.Map;

/**
 * The common Logger interface.
 *
 * @author Tony He
 */
public interface Logger {

    /**
     * Write a log in debug level.
     *
     * @param title   log title.
     * @param message log message.
     */
    void debug(String title, String message);

    /**
     * Write an exception log in debug level.
     *
     * @param title     log title.
     * @param throwable a Throwable instance.
     */
    void debug(String title, Throwable throwable);

    /**
     * Write a log in debug level with some extra attributes.
     *
     * @param title   log title
     * @param message log message
     * @param attrs   kv pairs
     */
    void debug(String title, String message, Map<String, String> attrs);

    /**
     * Write an exception log in debug level with some extra attributes
     *
     * @param title     log title
     * @param throwable a Throwable instance to log
     * @param attrs     kv pairs
     */
    void debug(String title, Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in debug level.
     *
     * @param message log message.
     */
    void debug(String message);

    /**
     * Write an exception log in debug level.
     *
     * @param throwable a Throwable instance.
     */
    void debug(Throwable throwable);

    /**
     * Write a log in debug level with some extra attributes.
     *
     * @param message log message
     * @param attrs   kv pairs
     */
    void debug(String message, Map<String, String> attrs);

    /**
     * Write an exception log in debug level with some extra attributes.
     *
     * @param throwable a Throwable instance
     * @param attrs     kv pairs
     */
    void debug(Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in info level.
     *
     * @param title   log title.
     * @param message log message.
     */
    void info(String title, String message);

    /**
     * Write an exception log in info level.
     *
     * @param title     log title.
     * @param throwable a Throwable instance.
     */
    void info(String title, Throwable throwable);

    /**
     * Write a log in info level with some extra attributes.
     *
     * @param title   log title
     * @param message log message
     * @param attrs   kv pairs
     */
    void info(String title, String message, Map<String, String> attrs);

    /**
     * Write an exception log in info level with some extra attributes.
     *
     * @param title     log title
     * @param throwable a Throwable instance to log
     * @param attrs     kv pairs
     */
    void info(String title, Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in info level.
     *
     * @param message log message.
     */
    void info(String message);

    /**
     * Write an exception log in info level.
     *
     * @param throwable a Throwable instance.
     */
    void info(Throwable throwable);

    /**
     * Write a log in info level with some extra attributes.
     *
     * @param message log message
     * @param attrs   kv pairs
     */
    void info(String message, Map<String, String> attrs);

    /**
     * Write an exception log in info level with some extra attributes.
     *
     * @param throwable a Throwable instance
     * @param attrs     kv pairs
     */
    void info(Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in warn level.
     *
     * @param title   log title.
     * @param message log message.
     */
    void warn(String title, String message);

    /**
     * Write an exception log in warn level.
     *
     * @param title     log title.
     * @param throwable a Throwable instance.
     */
    void warn(String title, Throwable throwable);

    /**
     * Write a log in warn level with some extra attributes.
     *
     * @param title   log title
     * @param message log message
     * @param attrs   kv pairs
     */
    void warn(String title, String message, Map<String, String> attrs);

    /**
     * Write an exception log in warn log with some extra attributes.
     *
     * @param title     log title
     * @param throwable a Throwable instance to log
     * @param attrs     kv pairs
     */
    void warn(String title, Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in warn level.
     *
     * @param message log message.
     */
    void warn(String message);

    /**
     * Write an exception log in warn level.
     *
     * @param throwable a Throwable instance.
     */
    void warn(Throwable throwable);

    /**
     * Write a log in warn level with some extra attributes.
     *
     * @param message log message
     * @param attrs   kv pairs
     */
    void warn(String message, Map<String, String> attrs);

    /**
     * Write an exception log in warn log with some extra attributes.
     *
     * @param throwable a Throwable instance
     * @param attrs     kv pairs
     */
    void warn(Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in error level.
     *
     * @param title   log title.
     * @param message log message.
     */
    void error(String title, String message);

    /**
     * Write an exception log in error level.
     *
     * @param title     log title.
     * @param throwable a Throwable instance.
     */
    void error(String title, Throwable throwable);

    /**
     * Write a log in error level with some extra attributes.
     *
     * @param title   log title
     * @param message log message
     * @param attrs   kv pairs
     */
    void error(String title, String message, Map<String, String> attrs);

    /**
     * Write an exception log in error level with some extra attributes.
     *
     * @param title     log title
     * @param throwable a Throwable instance to log
     * @param attrs     kv pairs
     */
    void error(String title, Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in error level.
     *
     * @param message log message.
     */
    void error(String message);

    /**
     * Write an exception log in error level.
     *
     * @param throwable a Throwable instance.
     */
    void error(Throwable throwable);

    /**
     * Write a log in error level with some extra attributes.
     *
     * @param message log message
     * @param attrs   kv pairs
     */
    void error(String message, Map<String, String> attrs);

    /**
     * Write an exception log in error level with some extra attributes.
     *
     * @param throwable a Throwable instance
     * @param attrs     kv pairs
     */
    void error(Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in fatal level.
     *
     * @param title   log title.
     * @param message log message.
     */
    void fatal(String title, String message);

    /**
     * Write an exception log in fatal level.
     *
     * @param title     log title.
     * @param throwable a Throwable instance.
     */
    void fatal(String title, Throwable throwable);

    /**
     * Write a log in fatal level with some extra attributes.
     *
     * @param title   log title
     * @param message log message
     * @param attrs   kv pairs
     */
    void fatal(String title, String message, Map<String, String> attrs);

    /**
     * Write an exception log in fatal level with some extra attributes.
     *
     * @param title     log title
     * @param throwable a Throwable instance to log
     * @param attrs     kv pairs
     */
    void fatal(String title, Throwable throwable, Map<String, String> attrs);

    /**
     * Write a log in fatal level.
     *
     * @param message log message.
     */
    void fatal(String message);

    /**
     * Write an exception log in fatal level.
     *
     * @param throwable a Throwable instance.
     */
    void fatal(Throwable throwable);

    /**
     * Write a log in fatal level with some extra attributes.
     *
     * @param message log message
     * @param attrs   kv pairs
     */
    void fatal(String message, Map<String, String> attrs);

    /**
     * Write an exception log in fatal level with some extra attributes.
     *
     * @param throwable a Throwable instance
     * @param attrs     kv pairs
     */
    void fatal(Throwable throwable, Map<String, String> attrs);
}
