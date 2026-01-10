package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.NotBlank;

public class FunilCreateDTO {

    @NotBlank(message = "O nome do funil não pode ser vazio.")
    private String nome;
    private String email;
    private String telefone;

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
}