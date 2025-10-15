package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etapa")
@Data
@NoArgsConstructor
public class Etapa { // <-- NÃO herda mais de BaseEntity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private int posicao; // Campo que estava faltando

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funil_id", nullable = false)
    @JsonIgnore
    private Funil funil;
}