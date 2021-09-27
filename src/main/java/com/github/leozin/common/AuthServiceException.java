package com.github.leozin.common;

public abstract class AuthServiceException extends RuntimeException {

    public AuthServiceException(String message) {
        super(message);
    }

    public abstract int httpCode();

}
