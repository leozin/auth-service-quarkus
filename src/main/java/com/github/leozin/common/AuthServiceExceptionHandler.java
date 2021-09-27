package com.github.leozin.common;

import com.github.leozin.dto.ErrorResponse;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Date;

@Provider
public class AuthServiceExceptionHandler implements ExceptionMapper<AuthServiceException> {
    @Override
    public Response toResponse(AuthServiceException exception) {
        return Response.status(exception.httpCode()).entity(
                new ErrorResponse(exception.getMessage(), exception.httpCode(), new Date())
        ).build();
    }
}
