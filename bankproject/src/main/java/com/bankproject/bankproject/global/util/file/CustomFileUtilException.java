package com.bankproject.bankproject.global.util.file;

public class CustomFileUtilException extends RuntimeException {
    public CustomFileUtilException(String message) {
        super(message);
    }

    public CustomFileUtilException(String message, Throwable cause) {
        super(message, cause);
    }
}
