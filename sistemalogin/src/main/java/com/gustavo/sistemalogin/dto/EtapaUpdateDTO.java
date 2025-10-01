package com.gustavo.sistemalogin.dto;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.gustavo.sistemalogin.model.BaseEntity;
import com.gustavo.sistemalogin.model.Funil;
import jakarta.persistence.Column;

public class EtapaUpdateDTO extends BaseEntity {






    private Funil funil;
    public EtapaUpdateDTO(Long id, String nome) {
        this.setId(id);
        this.setNome(nome);
    }

    public Funil getFunil() {
        return funil;
    }

    public void setFunil(Funil funil) {
        this.funil = funil;
    }

}
