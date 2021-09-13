package com.ants.common.utils;

import com.ants.common.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public class LogUtil {

    private static Logger logger = LoggerFactory.getLogger(CommonConstant.BUSINESS_LOGGER_NAME);

    private static final String logPrefix = " ";

    public static void setLogger(String name){
        logger = LoggerFactory.getLogger(name);
    }

    public static void info(String message, Object... objects){
        logger.info(logPrefix + message,objects);
    }

    public static void error(String message, Object... objects){
        logger.error(logPrefix + message,objects);
    }

    public static void debug(String message, Object... objects){
        logger.debug(logPrefix + message,objects);
    }
}
