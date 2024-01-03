package com.example.borys_mankowski_test_10.subscription.model;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {AppUserIdValidator.class})
@Documented
public @interface ExistingAppUser {
    String message() default "App User does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
