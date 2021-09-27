package com.github.leozin.common;

import com.github.leozin.dto.ErrorResponse;
import org.jboss.resteasy.spi.HttpResponseCodes;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.Date;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionHandler implements ExceptionMapper<ConstraintViolationException> {

    private static final String ERROR_MESSAGE = "%s (%s): %s";

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String message =
                exception.getConstraintViolations().stream().map
                                (o -> String.format(ERROR_MESSAGE, o.getPropertyPath(), o.getInvalidValue(), o.getMessage()))
                        .collect(Collectors.joining("\n"));
        return Response.status(HttpResponseCodes.SC_BAD_REQUEST).entity(
                new ErrorResponse(message, HttpResponseCodes.SC_BAD_REQUEST, new Date())
        ).build();
    }
}
