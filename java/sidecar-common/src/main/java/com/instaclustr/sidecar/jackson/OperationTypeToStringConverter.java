package com.instaclustr.sidecar.jackson;

import com.fasterxml.jackson.databind.util.StdConverter;
import com.instaclustr.sidecar.operations.OperationType;

public class OperationTypeToStringConverter extends StdConverter<OperationType, String> {

    @Override
    public String convert(final OperationType value) {
        if (value == null) {
            return null;
        }

        return value.toString().toLowerCase();
    }
}
