package com.mutantes.mutant_detector.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDnaSequenceValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDnaSequence {

    String message() default "La secuencia de ADN debe ser una matriz NxN (mínimo 4x4) " +
            "compuesta solo por los caracteres A, T, C y G.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}