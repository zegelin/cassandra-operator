package com.instaclustr.sidecar.operations;

import static java.util.stream.Collectors.toList;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractIdleService;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

public class OperationsService extends AbstractIdleService {
    private final ListeningExecutorService executorService;

    private final Map<Class<? extends OperationRequest>, OperationFactory> operationFactoriesByRequestType;

    private final Map<UUID, Operation> operations;

    @Inject
    public OperationsService(final Map<Class<? extends OperationRequest>, OperationFactory> operationFactoriesByRequestType,
                             final @OperationsMap Map<UUID, Operation> operations) {
        this.operationFactoriesByRequestType = operationFactoriesByRequestType;
        this.operations = operations;

        // TODO: custom executor implementation that allows for concurrent operations of different types
        this.executorService = MoreExecutors.listeningDecorator(Executors.newSingleThreadExecutor());
    }

    @Override
    protected void startUp() throws Exception {}

    @Override
    protected void shutDown() throws Exception {
        MoreExecutors.shutdownAndAwaitTermination(executorService, 1, TimeUnit.MINUTES);
    }

    public void submitOperation(final Operation operation) {
        operations.put(operation.id, operation);
        executorService.submit(operation);
    }

    public Operation submitOperationRequest(final OperationRequest request) {
        final OperationFactory operationFactory = operationFactoriesByRequestType.get(request.getClass());

        final Operation operation = operationFactory.createOperation(request);

        submitOperation(operation);

        return operation;
    }

    public Map<UUID, Operation> operations() {
        return Collections.unmodifiableMap(operations);
    }

    public Optional<Operation> operation(final UUID id) {
        return Optional.ofNullable(operations.get(id));
    }

    public Collection<Operation> operations(final OperationsFilter operationsFilter) {
        final Collection<Operation> operations = operationsByType(operationsFilter.type);

        if (operationsFilter.state != null) {
            return operationsByState(operations, Operation.State.valueOf(operationsFilter.state.toUpperCase()));
        }

        return operations;
    }

    private Collection<Operation> operationsByType(final OperationType operationType) {
        return operationsByType(Collections.unmodifiableCollection(operations.values()), operationType);
    }

    private Collection<Operation> operationsByState(final Operation.State state) {
        return operationsByState(Collections.unmodifiableCollection(operations.values()), state);
    }

    private Collection<Operation> operationsByState(final Collection<Operation> operations, final Operation.State state) {
        if (state == null) {
            return Collections.unmodifiableCollection(operations);
        }

        return operations.stream().filter(entry -> entry.state == state).collect(toList());
    }

    private Collection<Operation> operationsByType(final Collection<Operation> operations, final OperationType operationType) {
        if (operationType == null) {
            return Collections.unmodifiableCollection(operations);
        }

        return Collections.unmodifiableCollection(operations.stream().filter(entry -> entry.request.type.getClass() == operationType.getClass()).collect(toList()));
    }
}
