package org.ruoland.dictionary;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

public class DictionaryLogger {
    private final Logger logger;
    private static final List<String> errorMessages = new ArrayList<>();

    public DictionaryLogger(String name) {
        this.logger = LogManager.getLogger(name);
        Configurator.setRootLevel(Level.DEBUG);

        configureLogger();
        Configurator.setRootLevel(Level.DEBUG);

    }

    public DictionaryLogger(Class<?> clazz) {
        this(clazz.getName());
        Configurator.setLevel(clazz.getName(), Level.TRACE);
    }

    private void configureLogger() {
        Configurator.setLevel(logger.getName(), Level.TRACE);
        if (logger.isTraceEnabled()) {
            logger.info("Logger level successfully set to TRACE");
        } else {
            System.out.println("Failed to set logger level to TRACE for " + logger.getName());
        }
    }

    public void trace(String message, Object... obj) {
        if (logger.isTraceEnabled()) {
            logger.trace(addCustomInfo(message));
        }
    }

    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(addCustomInfo(message));
        }
    }
    public void debug(String message, Object... obj) {
        if (logger.isDebugEnabled()) {
            logger.debug(addCustomInfo(message), obj);
        }
    }

    public void info(String message) {
        if (logger.isInfoEnabled()) {
            logger.info(addCustomInfo(message));
        }
    }

    public void warn(String message) {
        logger.warn(addCustomInfo(message));
    }

    public void warn(String message, Object... obj) {
        logger.warn(addCustomInfo(message), obj);
    }
    public void error(String message) {
        logger.error(message);
        errorMessages.add(message);
    }

    public void error(String format, Object arg) {
        String msg = String.format(format, arg);
        error(msg);
    }

    public void error(String format, Object arg1, Object arg2) {
        String msg = String.format(format, arg1, arg2);
        error(msg);
    }

    public void error(String format, Object... arguments) {
        String msg = String.format(format, arguments);
        error(msg);
    }

    public void error(String msg, Throwable t) {
        logger.error(msg, t);
        errorMessages.add(msg + ": " + t.getMessage());
    }

    private String addCustomInfo(String message) {
        return "[CustomInfo] " + message;
    }

    public void logCurrentLevel() {
        System.out.println("Current log level for " + logger.getName() + ": " + getEffectiveLevel());
    }

    public Level getEffectiveLevel() {
        return ((org.apache.logging.log4j.core.Logger) logger).getLevel();
    }

    public static List<String> getErrorMessages() {
        return new ArrayList<>(errorMessages);
    }

    public static void clearErrorMessages() {
        errorMessages.clear();
    }

    public void info(String s, Object... obj) {
        logger.info(s, obj);
    }
}