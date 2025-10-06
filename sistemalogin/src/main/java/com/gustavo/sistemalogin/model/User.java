package com.gustavo.sistemalogin.model;

import jakarta.persistence.*;



@Entity
@Table(name = "users")
public class User  {


    @Column(name = "ativo")
    private Boolean active;

    private String email;

    private String nome;

    private int perfil;

    private Long id;

    private boolean isAtivo;

    public User(int perfil, Boolean active, String senha, String email, String nome) {
        this.perfil = perfil;
        this.active = active;
        this.senha = senha;
        this.setEmail(email);
        this.setNome(nome);
    }

    public Long getId() {return id;}

    public void setId(Long id) {this.id = id;}

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public boolean isAtivo() {return isAtivo;}

    public void setAtivo(boolean ativo) {isAtivo = ativo;}

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

    public User() {

    }





}