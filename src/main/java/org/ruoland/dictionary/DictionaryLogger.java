package org.ruoland.dictionary;

import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

import java.util.ArrayList;
import java.util.List;

public class DictionaryLogger {
    private final Logger logger;
    private static final List<String> errorMessages = new ArrayList<>();

    public DictionaryLogger(String name) {
        this.logger = LogManager.getLogger(name);
        Configurator.setRootLevel(Level.DEBUG);
        configureLogger();
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

    private String getCallerMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length >= 5) {
            return stackTrace[4].getMethodName();
        }
        return "Unknown";
    }

    private String addMethodInfo(String message) {
        return "[" + getCallerMethodName() + "] " + message;
    }

    public void trace(String message, Object... obj) {
        if (logger.isTraceEnabled()) {
            logger.trace(addMethodInfo(message), obj);
        }
    }

    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(addMethodInfo(message));
        }
    }

    public void debug(String message, Object... obj) {
        if (logger.isDebugEnabled()) {
            logger.debug(addMethodInfo(message), obj);
        }
    }

    public void info(String message) {
        if (logger.isInfoEnabled()) {
            logger.info(addMethodInfo(message));
        }
    }

    public void info(String message, Object... obj) {
        if (logger.isInfoEnabled()) {
            logger.info(addMethodInfo(message), obj);
        }
    }

    public void warn(String message) {
        logger.warn(addMethodInfo(message));
    }

    public void warn(String message, Object... obj) {
        logger.warn(addMethodInfo(message), obj);
    }

    public void error(String message) {
        String errorMsg = addMethodInfo(message);
        logger.error(errorMsg);
        errorMessages.add(errorMsg);
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
        String errorMsg = addMethodInfo(msg);
        logger.error(errorMsg, t);
        errorMessages.add(errorMsg + ": " + t.getMessage());
    }

    public void logCurrentLevel() {
        System.out.println("Current log level for " + logger.getName() + ": " + getEffectiveLevel());
    }
    private static final int MAX_LENGTH = 50; // 최대 로그 길이

    public static void log(String message, Object... params) {
        String fullMessage = String.format(message, params);
        String shortMessage = (fullMessage.length() > MAX_LENGTH)
                ? fullMessage.substring(0, MAX_LENGTH - 3) + "..."
                : fullMessage;

        Dictionary.LOGGER.info(shortMessage);
    }

    public static void logItem(ItemStack item, String description) {
        String itemId = item.getDescriptionId();
        String shortId = itemId.substring(itemId.lastIndexOf('.') + 1);
        String shortDesc = (description.length() > 30)
                ? description.substring(0, 27) + "..."
                : description;

        log("Item %s: %s", shortId, shortDesc);
    }
    public Level getEffectiveLevel() {
        return logger.getLevel();
    }

    public static List<String> getErrorMessages() {
        return new ArrayList<>(errorMessages);
    }

    public static void clearErrorMessages() {
        errorMessages.clear();
    }
}