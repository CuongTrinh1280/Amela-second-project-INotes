package com.assignment.edu.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UniqueConstraintValidator.class)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueConstraint {

    String message() default "Type category is already registered";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String property() default "type_id";

    Class<?> entity();
}
