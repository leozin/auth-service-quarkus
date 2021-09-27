package com.github.leozin.service;

import com.github.leozin.common.AuthServiceException;
import org.jboss.resteasy.spi.HttpResponseCodes;

public class UserNotFoundException extends AuthServiceException {

    public UserNotFoundException(String email) {
        super("User " + email + " not found");
    }

    public int httpCode() {
        return HttpResponseCodes.SC_NOT_FOUND;
    }

}
