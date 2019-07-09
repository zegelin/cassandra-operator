package com.instaclustr.sidecar.operations;

public interface OperationType {

    String toString();

    static final OperationType UNKNOWN = new OperationType() {
        @Override
        public String toString() {
            return "UNKNOWN";
        }
    };
}
