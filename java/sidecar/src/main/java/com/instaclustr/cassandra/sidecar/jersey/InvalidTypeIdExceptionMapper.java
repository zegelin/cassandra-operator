package com.instaclustr.cassandra.sidecar.jersey;

import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class InvalidTypeIdExceptionMapper implements ExceptionMapper<InvalidTypeIdException> {
    @Override
    public Response toResponse(final InvalidTypeIdException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(String.format(""))
                .build();
    }
}
