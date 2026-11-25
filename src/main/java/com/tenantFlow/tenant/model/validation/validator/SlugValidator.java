package com.tenantFlow.tenant.model.validation.validator;

import com.tenantFlow.tenant.model.validation.annotation.ValidSlug;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Set;
import java.util.regex.Pattern;

public class SlugValidator
        implements ConstraintValidator<ValidSlug, String> {

    private static final Set<String> RESERVED_SLUGS =
            Set.of("admin", "api", "www", "mail",
                    "support", "app", "login");

    private static final Pattern SLUG_PATTERN =
            Pattern.compile("^[a-z0-9]+(-[a-z0-9]+)*$");

    @Override
    public boolean isValid(
            String value,
            ConstraintValidatorContext context
    ) {

        if (value == null) {
            return true;
        }

        return value.length() >= 3
                && value.length() <= 63
                && SLUG_PATTERN.matcher(value).matches()
                && !RESERVED_SLUGS.contains(value);
    }
}