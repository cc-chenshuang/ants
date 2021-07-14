package com.ants.common.exception;

public class AntsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AntsException(String message) {
        super(message);
    }

    public AntsException(Throwable cause) {
        super(cause);
    }

    public AntsException(String message, Throwable cause) {
        super(message, cause);
    }
}
