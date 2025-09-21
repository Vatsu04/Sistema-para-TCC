package com.gustavo.sistemalogin.model;

import jakarta.persistence.*;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Entity
@Table(name = "roles") // ou "perfis"
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nome", nullable = false, unique = true) // Coluna com os nomes "ADMINISTRADOR", "ASSISTENTE"
    private String nome;

}