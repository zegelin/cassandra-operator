package com.instaclustr.cassandra.sidecar.resource;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.instaclustr.cassandra.sidecar.operations.Operation;
import com.instaclustr.cassandra.sidecar.operations.OperationRequest;
import com.instaclustr.cassandra.sidecar.operations.OperationsService;
import org.checkerframework.checker.nullness.qual.Nullable;

@Path("/operations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OperationsResource {
    private final OperationsService operationsService;

    @Inject
    public OperationsResource(final OperationsService operationsService) {
        this.operationsService = operationsService;
    }

    @GET
    public Collection<Operation> getOperations(@QueryParam("type") final Set<Class<? extends Operation>> operationTypesFilter) {
        Collection<Operation> operations = operationsService.operations().values();

        if (!operationTypesFilter.isEmpty()) {
            operations = Collections2.filter(operations, input -> operationTypesFilter.contains(input.getClass()));
        }

        return operations;
    }

    @GET
    @Path("{id}")
    public Operation getOperationById(@PathParam("id") final UUID id) {
        final Operation operation = operationsService.operations().get(id);

        if (operation == null) {
            throw new NotFoundException();
        }

        return operation;
    }

    @POST
    public Response createNewOperation(final OperationRequest request) {
        final Operation operation = operationsService.submitOperationRequest(request);

        final URI operationLocation = UriBuilder.fromResource(OperationsResource.class)
                .path(OperationsResource.class, "getOperationById")
                .build(operation.id);

        return Response.created(operationLocation).entity(operation).build();
    }

}
