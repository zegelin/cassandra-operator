package com.instaclustr.cassandra.sidecar.operations.cleanup;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.instaclustr.cassandra.sidecar.operations.OperationRequest;

import java.util.Set;

public class CleanupOperationRequest extends OperationRequest {
    public final String keyspace;
    public final Set<String> tables;

    @JsonCreator
    public CleanupOperationRequest(@JsonProperty("keyspace") final String keyspace,
                                   @JsonProperty("tables") final Set<String> tables) {
        this.keyspace = keyspace;
        this.tables = tables;
    }
}
