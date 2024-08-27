package org.ruoland.dictionary;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DictionaryLogger {
    private final Logger logger;
    private static final ArrayList<String> errorMessages = new ArrayList<>();
    private final ArrayList<String> warnList = new ArrayList<>();

    public DictionaryLogger(String name) {
        this.logger = LoggerFactory.getLogger(name);
    }
    public DictionaryLogger(Class<?> clazz) {
        this.logger = LoggerFactory.getLogger(clazz);
    }


    public void trace(String message) {
        if (logger.isTraceEnabled()) {
            logger.trace(addCustomInfo(message));
        }
    }

    public void trace(String message, Object... obj) {
        if (logger.isTraceEnabled()) {
            logger.trace(addCustomInfo(message), obj);
        }
    }
    public void debug(String message) {
        if (logger.isDebugEnabled()) {
            logger.debug(addCustomInfo(message));
        }
    }

    public void debug(String message, Object... obj){
        if(logger.isDebugEnabled())
            logger.debug(message, obj);
    }

    public void info(String message, Object... obj){
        if(logger.isInfoEnabled())
            logger.info(message, obj);
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
        logger.warn(message, obj);
    }

    public void error(String msg) {
        logger.error(msg);
        errorMessages.add(msg);
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
        // 여기에 커스텀 정보를 추가하는 로직을 구현합니다.
        // 예: 현재 시간, 스레드 ID, 사용자 정보 등
        return "[CustomInfo] " + message;
    }
    public static List<String> getErrorMessages() {
        return new ArrayList<>(errorMessages);
    }

    public static void clearErrorMessages() {
        errorMessages.clear();
    }
}