package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
// import jakarta.validation.constraints.PositiveOrZero; // <-- Removido

public class EtapaCreateDTO {

    @NotBlank(message = "O nome da etapa é obrigatório.")
    private String nome;

    // --- CAMPO REMOVIDO ---
    // A posição será calculada automaticamente pelo backend.
    /*
    @NotNull(message = "A posição da etapa é obrigatória.")
    @PositiveOrZero(message = "A posição deve ser um número positivo ou zero.")
    private Integer posicao;
    */
    // --- FIM DA MODIFICAÇÃO ---

    @NotNull(message = "O ID do funil é obrigatório.")
    private Long funilId;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    // --- GETTERS E SETTERS DE 'posicao' REMOVIDOS ---
    /*
    public Integer getPosicao() {
        return posicao;
    }

    public void setPosicao(Integer posicao) {
        this.posicao = posicao;
    }
    */
    // --- FIM DA MODIFICAÇÃO ---

    public Long getFunilId() {
        return funilId;
    }

    public void setFunilId(Long funilId) {
        this.funilId = funilId;
    }
}