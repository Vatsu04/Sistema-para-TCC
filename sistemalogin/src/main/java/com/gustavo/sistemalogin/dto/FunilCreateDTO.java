package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Negocio;
import com.gustavo.sistemalogin.model.User;

import java.util.List;

public class FunilCreateDTO  {

    private String nome;
    private String email;
    private User user;
    private List<Negocio> negocios;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Negocio> getNegocios() {
        return negocios;
    }

    public void setNegocios(List<Negocio> negocios) {
        this.negocios = negocios;
    }
}
