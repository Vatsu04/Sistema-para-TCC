package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Funil;
import jakarta.validation.constraints.PositiveOrZero;


public class EtapaUpdateDTO {

    private String nome;

    @PositiveOrZero(message = "A posição deve ser um número positivo ou zero.")
    private Integer posicao;

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
}
