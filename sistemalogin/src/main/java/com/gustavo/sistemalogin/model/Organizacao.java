package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "organizacoes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Organizacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true)
    private String cnpj;

    @Column
    private String telefone;

    @Column
    private String email;

    @Column
    private String endereco;

    // --- Relacionamentos ---

    // Muitas organizações podem ser cadastradas por um usuário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Evita que os dados do usuário venham junto com a organização
    private User user;
}
