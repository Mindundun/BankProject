package com.bankproject.bankproject.global.exception;

public class ServiceSystemException extends RuntimeException {

    public ServiceSystemException(String message) {
        super(message);
    }

    public ServiceSystemException(String message, Throwable cause) {
        super(message, cause);
    }

}
