package com.griddynamics.cd.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class NotEmptyOrNullValidator implements ConstraintValidator<NotEmptyOrNull, List<?>> {

    @Override
    public void initialize(NotEmptyOrNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<?> value, ConstraintValidatorContext context) {
        return value == null || !value.isEmpty();
    }
}
