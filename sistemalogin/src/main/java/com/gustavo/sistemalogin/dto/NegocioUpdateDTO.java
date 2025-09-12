package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Funil;
import com.gustavo.sistemalogin.model.Pessoa;

public class NegocioUpdateDTO {
    private String titulo;
    private int pipeline_stage;
    private int id_organizacao;
    private String etapa;
    private Funil funil;
    private Pessoa pessoa;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getPipeline_stage() {
        return pipeline_stage;
    }

    public void setPipeline_stage(int pipeline_stage) {
        this.pipeline_stage = pipeline_stage;
    }

    public int getId_organizacao() {
        return id_organizacao;
    }

    public void setId_organizacao(int id_organizacao) {
        this.id_organizacao = id_organizacao;
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
}
