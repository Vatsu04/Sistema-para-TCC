package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Funil;


public class EtapaUpdateDTO {

    private String nome;
    private Funil funil;

    public String getNome() {return nome;}

    public void setNome(String nome) {this.nome = nome;}

    public Funil getFunil() {
        return funil;
    }

    public void setFunil(Funil funil) {
        this.funil = funil;
    }

}
