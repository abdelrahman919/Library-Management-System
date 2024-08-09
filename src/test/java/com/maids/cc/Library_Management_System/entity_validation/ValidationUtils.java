package com.maids.cc.Library_Management_System.entity_validation;

import jakarta.validation.ConstraintViolation;

import java.util.Set;
import java.util.stream.Collectors;

public class ValidationUtils {

    protected static <T> Set<String> extractErrorMessages(Set<ConstraintViolation<T>> violations) {
        return violations.stream().map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());
    }


}
