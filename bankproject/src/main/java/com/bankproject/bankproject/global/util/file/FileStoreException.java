package com.bankproject.bankproject.global.util.file;

public class FileStoreException extends RuntimeException {
    public FileStoreException(String message) {
        super(message);
    }

    public FileStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
