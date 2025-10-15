package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class EtapaCreateDTO {

    @NotBlank(message = "O nome da etapa é obrigatório.")
    private String nome;

    @NotNull(message = "A posição da etapa é obrigatória.")
    @PositiveOrZero(message = "A posição deve ser um número positivo ou zero.")
    private Integer posicao;

    @NotNull(message = "O ID do funil é obrigatório.")
    private Long funilId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }

    public Long getFunilId() {
        return funilId;
    }

    public void setFunilId(Long funilId) {
        this.funilId = funilId;
    }
}