package com.gustavo.sistemalogin.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;


@Entity
@Table(name = "roles") // ou "perfis"
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, unique = true) // Coluna com os nomes "ADMINISTRADOR", "ASSISTENTE"
    private String nome;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}