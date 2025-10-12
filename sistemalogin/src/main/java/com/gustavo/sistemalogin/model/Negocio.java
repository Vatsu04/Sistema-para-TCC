package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "negocios")
@Data
@NoArgsConstructor
public class Negocio { // <-- NÃO herda mais de BaseEntity

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private String etapa;

    private LocalDate data_de_fechamento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funil_id", nullable = false)
    @JsonIgnore
    private Funil funil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    @JsonIgnore
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizacao_id") // Nome da coluna no banco
    @JsonIgnore
    private Organizacao organizacao;
}