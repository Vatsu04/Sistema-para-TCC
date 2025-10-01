package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

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
    private BigDecimal valor;


    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public int getId_ornizacao() {
        return id_ornizacao;
    }

    public void setId_ornizacao(int id_ornizacao) {
        this.id_ornizacao = id_ornizacao;
    }

    public int getPipeline_stage() {
        return pipeline_stage;
    }

    public void setPipeline_stage(int pipeline_stage) {
        this.pipeline_stage = pipeline_stage;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }

    public Funil getFunil() {
        return funil;
    }

    public void setFunil(Funil funil) {
        this.funil = funil;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

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
