package com.instaclustr.cassandra.sidecar.operations;

import javax.inject.Inject;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.instaclustr.jackson.MapBackedTypeIdResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("WeakerAccess")
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type")
@JsonTypeIdResolver(Operation.TypeIdResolver.class)
public abstract class Operation<RequestT extends OperationRequest> implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(Operation.class);

    public static class TypeIdResolver extends MapBackedTypeIdResolver<Operation> {
        @Inject
        public TypeIdResolver(final Map<String, Class<? extends Operation>> typeMappings) {
            super(typeMappings);
        }
    }

    public enum State {
        PENDING, RUNNING, COMPLETED, FAILED
    }

    public final UUID id = UUID.randomUUID();
    public final Instant creationTime = Instant.now();

    @JsonUnwrapped // embed the request parameters in the serialized output directly
    public final RequestT request;

    public State state = State.PENDING;
    public Throwable failureCause;
    public float progress = Float.NaN;
    public Instant startTime, completionTime;

    protected Operation(final RequestT request) {
        this.request = request;
    }

    @Override
    public final void run() {
        state = State.RUNNING;
        startTime = Instant.now();

        try {
            run0();
            progress = 1;
            state = State.COMPLETED;

        } catch (final Throwable t) {
            logger.error(String.format("Operation %s has failed.", id), t);
            state = State.FAILED;
            failureCause = t;

        } finally {
            completionTime = Instant.now();
        }
    }

    protected abstract void run0() throws Exception;
}
