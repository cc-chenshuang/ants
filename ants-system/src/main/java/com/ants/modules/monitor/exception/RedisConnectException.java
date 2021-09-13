package com.ants.modules.monitor.exception;


/**
 * TODO
 * Author Chen
 * Date   2021/9/7 18:44
 */
public class RedisConnectException extends Exception {

    private static final long serialVersionUID = 1639374111871115063L;

    public RedisConnectException(String message) {
        super(message);
    }
}
