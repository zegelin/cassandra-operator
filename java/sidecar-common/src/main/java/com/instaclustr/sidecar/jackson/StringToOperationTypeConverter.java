package com.instaclustr.sidecar.jackson;

import javax.inject.Inject;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.instaclustr.sidecar.operations.OperationType;

public class StringToOperationTypeConverter extends StdConverter<String, OperationType> {

    private final Map<String, OperationType> stringOperationTypeMap;

    @Inject
    public StringToOperationTypeConverter(final Map<String, OperationType> stringOperationTypeMap) {
        this.stringOperationTypeMap = stringOperationTypeMap;
    }

    @Override
    public OperationType convert(final String value) {
        if (value == null) {
            return null;
        }

        return Optional.ofNullable(stringOperationTypeMap.get(value.toLowerCase())).orElse(OperationType.UNKNOWN);
    }
}
