package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.enums.PerfilUsuario;

public class UserCreateDTO {
    private String nome;
    private String email;
    private String senha;
    private Boolean ativo;

    private PerfilUsuario perfil;

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

    public String getSenha() {
        return senha;
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

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
