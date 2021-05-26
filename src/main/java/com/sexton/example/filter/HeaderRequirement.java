package com.sexton.example.filter;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.Set;

@FunctionalInterface
public interface HeaderRequirement<T> {
    Set<ConstraintViolation<T>> satisfied(HttpServletRequest request);
}
