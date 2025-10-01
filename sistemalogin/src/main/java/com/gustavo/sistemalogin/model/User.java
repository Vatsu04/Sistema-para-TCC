package com.gustavo.sistemalogin.model;

import jakarta.persistence.*;



@Entity
@Table(name = "users")
public class User extends BaseEntity {

    public User() {

    }



    public User(int perfil, Boolean active, String senha, String email, String nome) {
        this.perfil = perfil;
        this.active = active;
        this.senha = senha;
        this.setEmail(email);
        this.setNome(nome);
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getPerfil() {
        return perfil;
    }

    public void setPerfil(int perfil) {
        this.perfil = perfil;
    }

    private String senha;

    @Column(name = "ativo")
    private Boolean active;


    private int perfil;


}