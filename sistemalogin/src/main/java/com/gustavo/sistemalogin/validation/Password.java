package com.gustavo.sistemalogin.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotação para validar se uma senha atende aos critérios de segurança definidos.
 */
@Constraint(validatedBy = PasswordValidator.class) // Aponta para a classe que contém a lógica
@Target({ElementType.FIELD, ElementType.METHOD})   // Onde a anotação pode ser usada
@Retention(RetentionPolicy.RUNTIME)               // Quando a anotação será processada
public @interface Password {

    // Mensagem de erro padrão que será exibida se a validação falhar
    String message() default "A senha não atende aos requisitos de segurança. Deve conter pelo menos uma letra maiúscula, uma minúscula, um número, um caractere especial e ter no mínimo 8 caracteres.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}