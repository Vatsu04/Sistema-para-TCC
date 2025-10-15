package com.gustavo.sistemalogin.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class NegocioUpdateDTO {


    private String titulo;
    private BigDecimal valor;
    private LocalDate data_de_fechamento;
    private Long etapaId;
    private Long funilId;
    private Long pessoaId;
    private String organizacao;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getData_de_fechamento() {
        return data_de_fechamento;
    }

    public void setData_de_fechamento(LocalDate data_de_fechamento) {
        this.data_de_fechamento = data_de_fechamento;
    }

    public Long getEtapaId() {
        return etapaId;
    }

    public void setEtapaId(Long etapaId) {
        this.etapaId = etapaId;
    }

    public Long getFunilId() {
        return funilId;
    }

    public void setFunilId(Long funilId) {
        this.funilId = funilId;
    }

    public Long getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Long pessoaId) {
        this.pessoaId = pessoaId;
    }

    public String getOrganizacao() {
        return organizacao;
    }

    public void setOrganizacao(String organizacao) {
        this.organizacao = organizacao;
    }
}