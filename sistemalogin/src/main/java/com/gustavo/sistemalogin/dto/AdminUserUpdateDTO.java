package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import com.gustavo.sistemalogin.validation.Password; // <-- IMPORTE A ANOTAÇÃO
import jakarta.validation.constraints.Email;


public class AdminUserUpdateDTO {

    private String nome;

    @Email(message = "O email deve ser válido (se informado).")
    private String email;

    @Password // Valida a senha apenas se ela for fornecida
    private String senha;

    private Boolean ativo;

    private PerfilUsuario perfil;

    // Getters e Setters
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public PerfilUsuario getPerfil() {
        return perfil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
}