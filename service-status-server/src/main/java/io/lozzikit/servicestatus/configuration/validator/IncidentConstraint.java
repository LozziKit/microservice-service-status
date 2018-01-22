package io.lozzikit.servicestatus.configuration.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IncidentValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IncidentConstraint {
    String message() default "Invalid Incident";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

