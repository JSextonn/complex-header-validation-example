package com.sexton.example.filter;

import lombok.val;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public abstract class BaseHeaderRequirement<T> implements HeaderRequirement<T> {
    private final Validator validator;

    public BaseHeaderRequirement() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public Set<ConstraintViolation<T>> satisfied(HttpServletRequest request) {
        val spec = buildValidationSpec(request);
        return validator.validate(spec);
    }

    protected abstract T buildValidationSpec(HttpServletRequest request);
}
