package com.github.leozin.service;

import com.github.leozin.common.AuthServiceException;
import org.jboss.resteasy.spi.HttpResponseCodes;

public class InvalidCredentialsException extends AuthServiceException {

    public InvalidCredentialsException() {
        super("Email and password do not match");
    }

    public int httpCode() {
        return HttpResponseCodes.SC_UNAUTHORIZED;
    }

}
