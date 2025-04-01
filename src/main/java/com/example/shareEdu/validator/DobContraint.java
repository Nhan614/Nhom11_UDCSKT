package com.example.shareEdu.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = { DobValidator.class })
public @interface DobContraint {
    String message() default "Invalid Date Format";

    int min();

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
