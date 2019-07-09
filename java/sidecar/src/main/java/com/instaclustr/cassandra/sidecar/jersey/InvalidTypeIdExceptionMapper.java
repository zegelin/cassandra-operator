package com.instaclustr.cassandra.sidecar.jersey;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * An {@see ExceptionMapper} for {@see InvalidTypeIdException}s. which are thrown by Jackson
 * when it's unable to determine the type for a given type Id.
 *
 * For example, this exception will be thrown when an unknown OperationRequest type is submitted.
 */
@Provider
public class InvalidTypeIdExceptionMapper implements ExceptionMapper<InvalidTypeIdException> {
    @Override
    public Response toResponse(final InvalidTypeIdException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(String.format("Unknown %s type \"%s\"",
                        exception.getBaseType().getRawClass().getSimpleName(),
                        exception.getTypeId()))
                .build();
    }
}
