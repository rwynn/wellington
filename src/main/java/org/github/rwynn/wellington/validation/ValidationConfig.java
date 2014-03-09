package org.github.rwynn.wellington.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Configuration
public class ValidationConfig {

    @Bean
    LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    StrongPasswordRules strongPasswordRules() {
        return new StrongPasswordRules();
    }

    public static class StrongPasswordValidator implements ConstraintValidator<StrongPassword, CharSequence> {

        @Autowired
        StrongPasswordRules strongPasswordRules;

        @Override
        public void initialize(StrongPassword constraintAnnotation) {

        }

        @Override
        public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
            return value != null && this.strongPasswordRules.applyRules(value.toString()).isValid();
        }
    }
}
