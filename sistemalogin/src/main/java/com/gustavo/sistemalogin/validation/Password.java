package com.gustavo.sistemalogin.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {

    String message() default "A senha não atende aos requisitos de segurança. Deve conter pelo menos uma letra maiúscula, uma minúscula, um número, um caractere especial e ter no mínimo 8 caracteres.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}