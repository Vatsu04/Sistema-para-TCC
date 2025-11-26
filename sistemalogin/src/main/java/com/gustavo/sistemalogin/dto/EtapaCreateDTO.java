package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EtapaCreateDTO {

    @NotBlank(message = "O nome da etapa é obrigatório.")
    private String nome;



    @NotNull(message = "O ID do funil é obrigatório.")
    private Long funilId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }



    public Long getFunilId() {
        return funilId;
    }

    public void setFunilId(Long funilId) {
        this.funilId = funilId;
    }
}