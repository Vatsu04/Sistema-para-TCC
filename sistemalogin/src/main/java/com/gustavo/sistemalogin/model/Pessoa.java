package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "pessoas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true) // Opcional, mas uma boa prática para contatos
    private String email;

    private String telefone;

    // --- Relacionamentos ---

    // Muitas pessoas (contatos) podem pertencer a um usuário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

    // Uma pessoa pode estar associada a uma lista de negócios.
    @OneToMany(mappedBy = "pessoa")
    @JsonIgnore // Evita carregar todos os negócios ao buscar uma pessoa
    private List<Negocio> negocios;
}
