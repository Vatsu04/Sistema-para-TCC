package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.NotBlank;

public class FunilCreateDTO {
    @NotBlank(message = "O nome do funil não pode ser vazio.")
    private String nome;

    // Apenas getter e setter do nome
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
}