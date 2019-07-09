package com.instaclustr.sidecar.operations;

import static java.util.stream.Collectors.toSet;

import java.time.Instant;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.AbstractScheduledService;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class OperationsExpirationService extends AbstractScheduledService {
    private final int expirationPeriodInSeconds;
    private final Map<UUID, Operation> operations;

    @Inject
    public OperationsExpirationService(final @OperationsMap Map<UUID, Operation> operations,
                                       final @Named("operationsExpirationPeriod") int expirationPeriodInSeconds) {
        this.operations = operations;
        this.expirationPeriodInSeconds = expirationPeriodInSeconds;
    }

    @Override
    protected void runOneIteration() throws Exception {
        final Instant expirationThreshold = Instant.now().minusSeconds(expirationPeriodInSeconds);

        final Iterator<Operation> operationsIterator = operations.values().iterator();

        while (operationsIterator.hasNext()) {
            final Operation operation = operationsIterator.next();

            if (!operation.state.isTerminalState()) {
                continue;
            }

            if (operation.completionTime.isBefore(expirationThreshold)) {
                continue;
            }

            operationsIterator.remove();
        }
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedRateSchedule(expirationPeriodInSeconds, expirationPeriodInSeconds, TimeUnit.SECONDS);
    }
}
