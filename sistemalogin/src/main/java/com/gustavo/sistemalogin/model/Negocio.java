package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "negocios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Negocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column
    private int pipeline_stage;


    @Column
    private int id_ornizacao;

    // Ex: "Contato Inicial", "Proposta Enviada", "Negociação", "Ganha", "Perdida"
    @Column(nullable = false)
    private String etapa;


    // Muitos negócios pertencem a um funil.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funil_id", nullable = false)
    @JsonIgnore
    private Funil funil;

    // Muitos negócios podem estar associados a uma pessoa (contato).
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    @JsonIgnore
    private Pessoa pessoa;
}
