package com.instaclustr.cassandra.sidecar.jersey;

import com.google.inject.TypeLiteral;
import com.instaclustr.cassandra.sidecar.operations.Operation;

import javax.inject.Inject;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
public class OperationTypeIdParamConverterProvider implements ParamConverterProvider {
    private static Type PARAMETER_TYPE = new TypeLiteral<Class<? extends Operation>>() {}.getType();

    private final Operation.TypeIdResolver typeIdResolver;

    @Inject
    public OperationTypeIdParamConverterProvider(final Operation.TypeIdResolver typeIdResolver) {
        this.typeIdResolver = typeIdResolver;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> ParamConverter<T> getConverter(final Class<T> rawType, final Type genericType, final Annotation[] annotations) {
        if (!genericType.equals(PARAMETER_TYPE)) {
            return null;
        }

        return (ParamConverter<T>) new ParamConverter<Class<? extends Operation>>() {
            @Override
            public Class<? extends Operation> fromString(final String value) {
                final Class<? extends Operation> clazz = typeIdResolver.classFromId(value);

                if (clazz == null) {
                    // TODO: map this to a 400 Bad Request
                    throw new IllegalArgumentException(String.format("Unrecognised Operation type \"%s\"", value));
                }

                return clazz;
            }

            @Override
            public String toString(final Class<? extends Operation> value) {
                if (value == null) {
                    throw new IllegalArgumentException();
                }

                return typeIdResolver.idFromValue(value);
            }
        };
    }
}
