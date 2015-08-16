package com.xqbase.bn.common.logging.slf4j;

import com.xqbase.bn.common.logging.Logger;

import java.util.Map;

/**
 * Logger implementation based on Slf4j;
 *
 * @author Tony He
 */
public class Slf4jLogger implements Logger {

    private static final String MSG_WITH_TITLE = "{}: {}";
    private static final String MSG_WITH_TITLE_ATTRS = "{}: {}\n{}";
    private static final String MSG_WITH_ATTRS = "{}\n{}";

    private final org.slf4j.Logger logger;

    public Slf4jLogger(org.slf4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public void debug(String title, String message) {
        logger.debug(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void debug(String title, Throwable throwable) {
        logger.debug(title, throwable);
    }

    @Override
    public void debug(String title, String message, Map<String, String> attrs) {
        logger.debug(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void debug(String title, Throwable throwable, Map<String, String> attrs) {
        logger.debug(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void debug(String message) {
        logger.debug(message);
    }

    @Override
    public void debug(Throwable throwable) {
        logger.debug(throwable.getMessage(), throwable);
    }

    @Override
    public void debug(String message, Map<String, String> attrs) {
        logger.debug(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void debug(Throwable throwable, Map<String, String> attrs) {
        logger.debug(getAttributeText(attrs), throwable);
    }

    @Override
    public void info(String title, String message) {
        logger.info(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void info(String title, Throwable throwable) {
        logger.info(title, throwable);
    }

    @Override
    public void info(String title, String message, Map<String, String> attrs) {
        logger.info(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void info(String title, Throwable throwable, Map<String, String> attrs) {
        logger.info(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void info(String message) {
        logger.info(message);
    }

    @Override
    public void info(Throwable throwable) {
        logger.info(throwable.getMessage(), throwable);
    }

    @Override
    public void info(String message, Map<String, String> attrs) {
        logger.info(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void info(Throwable throwable, Map<String, String> attrs) {
        logger.info(getAttributeText(attrs), throwable);
    }

    @Override
    public void warn(String title, String message) {
        logger.warn(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void warn(String title, Throwable throwable) {
        logger.warn(title, throwable);
    }

    @Override
    public void warn(String title, String message, Map<String, String> attrs) {
        logger.warn(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void warn(String title, Throwable throwable, Map<String, String> attrs) {
        logger.warn(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void warn(String message) {
        logger.warn(message);
    }

    @Override
    public void warn(Throwable throwable) {
        logger.warn(throwable.getMessage(), throwable);
    }

    @Override
    public void warn(String message, Map<String, String> attrs) {
        logger.warn(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void warn(Throwable throwable, Map<String, String> attrs) {
        logger.warn(getAttributeText(attrs), throwable);
    }

    @Override
    public void error(String title, String message) {
        logger.error(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void error(String title, Throwable throwable) {
        logger.error(title, throwable);
    }

    @Override
    public void error(String title, String message, Map<String, String> attrs) {
        logger.error(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void error(String title, Throwable throwable, Map<String, String> attrs) {
        logger.error(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void error(String message) {
        logger.error(message);
    }

    @Override
    public void error(Throwable throwable) {
        logger.error(throwable.getMessage(), throwable);
    }

    @Override
    public void error(String message, Map<String, String> attrs) {
        logger.error(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void error(Throwable throwable, Map<String, String> attrs) {
        logger.error(getAttributeText(attrs), throwable);
    }

    @Override
    public void fatal(String title, String message) {
        logger.error(MSG_WITH_TITLE, title, message);
    }

    @Override
    public void fatal(String title, Throwable throwable) {
        logger.error(title, throwable);
    }

    @Override
    public void fatal(String title, String message, Map<String, String> attrs) {
        logger.error(MSG_WITH_TITLE_ATTRS, title, message, getAttributeText(attrs));
    }

    @Override
    public void fatal(String title, Throwable throwable, Map<String, String> attrs) {
        logger.error(title + "\n" + getAttributeText(attrs), throwable);
    }

    @Override
    public void fatal(String message) {
        logger.error(message);
    }

    @Override
    public void fatal(Throwable throwable) {
        logger.error(throwable.getMessage(), throwable);
    }

    @Override
    public void fatal(String message, Map<String, String> attrs) {
        logger.error(MSG_WITH_ATTRS, message, getAttributeText(attrs));
    }

    @Override
    public void fatal(Throwable throwable, Map<String, String> attrs) {
        logger.error(getAttributeText(attrs), throwable);
    }

    private String getAttributeText(Map<String, String> attrs) {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append(" ");
        }
        return builder.toString();
    }
}
