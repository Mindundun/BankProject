package com.bankproject.bankproject.global.exception;

public class CustomBaseException extends RuntimeException {
    private String methodName;
    private String className;

    public CustomBaseException(String message, String methodName, String className) {
        super(message);
        this.methodName = methodName;
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public String getClassName() {
        return className;
    }

}
