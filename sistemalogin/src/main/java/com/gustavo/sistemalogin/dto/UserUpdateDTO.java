package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.BaseEntity;
import com.gustavo.sistemalogin.model.enums.PerfilUsuario;

public class UserUpdateDTO extends BaseEntity {

    private String senha;
    private Boolean ativo;

    private PerfilUsuario perfil;



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
