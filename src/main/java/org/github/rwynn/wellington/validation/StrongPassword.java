package org.github.rwynn.wellington.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = { ValidationConfig.StrongPasswordValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
@NotNull
public @interface StrongPassword {

    String message() default "{org.github.rwynn.wellington.transfer.UserDTO.password.StrongPassword.message}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
