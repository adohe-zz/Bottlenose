package com.xqbase.baiji.common.logging;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Clients should call this class to get a Logger.
 *
 * @author Tony He
 */
public class LoggerFactory {

    private static final DummyLogger DUMMY_LOGGER = new DummyLogger();

    private static ILoggerFactory currentFactory = null;
    private static ConcurrentHashMap<String, ILoggerFactory> loggerFactoryMap = new ConcurrentHashMap<>();

    static {

    }

    /**
     * Register an ILoggerFactory instance to the system.
     */
    public static void register(ILoggerFactory factory) {
        loggerFactoryMap.put(factory.name(), factory);
        if (loggerFactoryMap.size() == 1) {
            currentFactory = factory;
        }
    }

    /**
     * Set the ILoggerFactory to be used in the system.
     *
     * @param name the name of ILoggerFactory to use
     */
    public static void setCurrentFactory(String name) {
        ILoggerFactory factory = loggerFactoryMap.get(name);
        if (factory != null) {
            currentFactory = factory;
        }
    }

    /**
     * Return a logger named corresponding to the class passed as parameter, using
     * the statically bound {@link ILoggerFactory} instance.
     *
     * @param clazz the returned logger will be named after clazz
     * @return logger
     */
    public static Logger getLogger(Class clazz) {
        return currentFactory != null ? currentFactory.getLogger(clazz) : DUMMY_LOGGER;
    }

    /**
     * Return an appropriate {@link Logger} instance as specified by the
     * <code>name</code> parameter.
     * <p/>
     * <p>Null-valued name arguments are considered invalid.
     *
     * @param name the name of the Logger to return
     * @return a Logger instance
     */
    public static Logger getLogger(String name) {
        return currentFactory != null ? currentFactory.getLogger(name) : DUMMY_LOGGER;
    }
}
