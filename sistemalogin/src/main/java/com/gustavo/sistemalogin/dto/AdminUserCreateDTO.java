package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import com.gustavo.sistemalogin.validation.Password; // <-- IMPORTE A NOVA ANOTAÇÃO
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminUserCreateDTO {

    @NotBlank(message = "O nome não pode ser vazio.")
    private String nome;

    @Email(message = "O email deve ser válido.")
    @NotBlank(message = "O email não pode ser vazio.")
    private String email;

    @NotBlank(message = "A senha não pode ser vazia.")
    @Password
    private String senha;

    @NotNull(message = "O perfil do usuário é obrigatório.")
    private PerfilUsuario perfil;

    @NotNull(message = "O status ativo/inativo é obrigatório.")
    private Boolean ativo;


}