package com.instaclustr.sidecar.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.MoreObjects;
import com.instaclustr.sidecar.jackson.OperationTypeToStringConverter;
import com.instaclustr.sidecar.jackson.StringToOperationTypeConverter;
import com.instaclustr.sidecar.validation.Enum;

public class OperationsFilter {

    @JsonSerialize(converter = OperationTypeToStringConverter.class)
    @JsonDeserialize(converter = StringToOperationTypeConverter.class)
    public final OperationType type;

    @Enum(enumClass = Operation.State.class, ignoreCase = true)
    public final String state;

    @JsonCreator
    public OperationsFilter(final @JsonProperty("type") OperationType type,
                            final @JsonProperty("state") String state) {
        this.type = type;
        this.state = state;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("type", type)
                .add("state", state)
                .toString();
    }
}
