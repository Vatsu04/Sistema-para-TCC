package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Funil;
import jakarta.persistence.Column;

public class EtapaUpdateDTO {
    private Long id;

    @Column(nullable = false)
    private String nome;

    public Long getId() {
        return id;
    }

    public Funil getFunil() {
        return funil;
    }

    public void setFunil(Funil funil) {
        this.funil = funil;
    }

    public Long getFunilId(Funil funil){
        return funil.getId();
    }

    private Funil funil;
    public EtapaUpdateDTO(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
