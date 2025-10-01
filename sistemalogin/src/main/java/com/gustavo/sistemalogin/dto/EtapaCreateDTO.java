package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.BaseEntity;
import com.gustavo.sistemalogin.model.Funil;
import jakarta.persistence.Column;

public class EtapaCreateDTO extends BaseEntity {

    private Funil funil;
    public EtapaCreateDTO(Long id, String nome) {
        this.setId(id);
        this.setNome(nome);
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
}
