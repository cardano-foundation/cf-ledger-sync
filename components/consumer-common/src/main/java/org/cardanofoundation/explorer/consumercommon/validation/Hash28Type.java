package org.cardanofoundation.explorer.consumercommon.validation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = Hash28TypeValidator.class)
@Documented
public @interface Hash28Type {
  String message() default "The value must be Hash28Type";

  Class<?>[] groups() default { };

  Class<? extends Payload>[] payload() default { };
}

