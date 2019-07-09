package com.instaclustr.sidecar.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<Enum, String> {

    private Enum annotation;

    @Override
    public void initialize(final Enum annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation();

        if (value == null) {
            return true;
        }

        final Object[] enumValues = this.annotation.enumClass().getEnumConstants();

        if (enumValues != null) {
            for (final Object enumValue : enumValues) {
                if (value.equals(enumValue.toString()) || (this.annotation.ignoreCase() && value.equalsIgnoreCase(enumValue.toString()))) {
                    return true;
                }
            }
        }

        if (enumValues != null) {
            final String message = String.format("Value '%s' is invalid for enumeration %s. Possible values: %s",
                                                 value,
                                                 this.annotation.enumClass(),
                                                 Arrays.stream(enumValues).map(Object::toString).collect(Collectors.joining(",")));

            context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
        }

        return false;
    }
}
