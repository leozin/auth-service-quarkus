package com.github.leozin.service;

import com.github.leozin.common.AuthServiceException;

public class UserAlreadyExistsException extends AuthServiceException {

    public UserAlreadyExistsException(String email) {
        super("User " + email + " is already registered");
    }

    public int httpCode() {
        // unprocessable
        return 422;
    }

}
