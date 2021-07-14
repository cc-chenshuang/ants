package com.ants.common.utils;

import com.ants.common.constant.CommonConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author wenda
 * @title
 * @date 2021/2/1 9:42
 * @desc
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
