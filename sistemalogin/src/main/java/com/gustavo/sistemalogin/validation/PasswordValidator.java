package com.gustavo.sistemalogin.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    // Regex para validar a senha:
    // ^                 # Início da string
    // (?=.*[0-9])       # Pelo menos um número
    // (?=.*[a-z])       # Pelo menos uma letra minúscula
    // (?=.*[A-Z])       # Pelo menos uma letra maiúscula
    // (?=.*[@#$%^&+=!]) # Pelo menos um caractere especial
    // (?=\S+$)          # Sem espaços em branco
    // .{8,}             # Pelo menos 8 caracteres
    // $                 # Fim da string
    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";

    private static final Pattern pattern = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        // Se a senha for nula, a anotação @NotBlank cuidará disso.
        // Aqui, consideramos nulo como válido para não duplicar a validação.
        if (password == null) {
            return true;
        }
        return pattern.matcher(password).matches();
    }
}