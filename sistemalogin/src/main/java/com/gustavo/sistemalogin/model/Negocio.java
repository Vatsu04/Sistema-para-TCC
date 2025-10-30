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
public class Negocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private BigDecimal valor;

    // --- CORREÇÃO: Removido 'updatable = false' ---
    @Column(name = "data_de_abertura", nullable = false)
    private LocalDate dataDeAbertura;

    @Column(name = "data_de_fechamento")
    private LocalDate dataDeFechamento;

    @Column(name = "organizacao")
    private String organizacao;

    // --- CAMPOS DESNORMALIZADOS ---
    @Column(name = "Pessoa_Contato", nullable = false)
    private String pessoaContato;

    @Column(name = "Email_Pessoa_Contato", nullable = false)
    private String emailPessoaContato;

    @Column(name = "Telefone_Pessoa_Contato", nullable = false)
    private String telefonePessoaContato;

    // --- RELACIONAMENTOS ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etapa_id", nullable = false)
    @JsonIgnore
    private Etapa etapa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funil_id", nullable = false)
    @JsonIgnore
    private Funil funil;
}