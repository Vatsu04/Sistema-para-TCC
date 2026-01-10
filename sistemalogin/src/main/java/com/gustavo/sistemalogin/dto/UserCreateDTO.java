package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.validation.Password; // <-- IMPORTE A NOVA ANOTAÇÃO
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreateDTO {

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @Email(message = "O email deve ser válido.")
    @NotBlank(message = "O email não pode ser vazio.")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia.")
    @Password // <-- ADICIONE A ANOTAÇÃO AQUI
    private String senha;
}