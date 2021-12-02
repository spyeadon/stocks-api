package com.springgradlesandbox.springgradlesandox.Validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUser {
    String message() default "ProductOffering request must include a memberNumber or memberID (or both)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default { };
}