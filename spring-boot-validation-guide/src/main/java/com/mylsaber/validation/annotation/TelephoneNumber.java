package com.mylsaber.validation.annotation;

import com.mylsaber.validation.validator.TelephoneNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author mylsaber
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {TelephoneNumberValidator.class})
public @interface TelephoneNumber {
    String message() default "Invalid telephone number";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
