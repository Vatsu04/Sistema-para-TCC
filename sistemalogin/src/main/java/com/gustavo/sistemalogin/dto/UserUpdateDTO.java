package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import com.gustavo.sistemalogin.validation.Password; // <-- IMPORTE A ANOTAÇÃO

public class UserUpdateDTO {

    @Password // <-- ADICIONE A ANOTAÇÃO AQUI
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

    public void setPerfil(PerfilUsuario perfil) {
        this.perfil = perfil;
    }
}