package com.maids.cc.Library_Management_System.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;

import java.lang.annotation.*;

/*
* This annotation validates phone number against Egyptian phone number standards
* */

@Target({ElementType.FIELD,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {})
@Pattern(regexp = "^0(10|11|12|15)[0-9]{8}$",
        message = "Please enter a valid number" )
public @interface Phone {

    public String message() default "Please enter a valid phone number.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
