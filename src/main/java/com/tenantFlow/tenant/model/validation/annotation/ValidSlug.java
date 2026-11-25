package com.tenantFlow.tenant.model.validation.annotation;

import com.tenantFlow.tenant.model.validation.validator.SlugValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SlugValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidSlug {

    String message() default "Invalid tenant slug";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}