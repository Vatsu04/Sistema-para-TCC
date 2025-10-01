package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.BaseEntity;

import java.time.LocalDate;

public class PessoaCreateDTO extends BaseEntity {

    private String cpf;
    private String rg;
    private LocalDate data_de_nascimento;


    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public LocalDate getData_de_nascimento() {
        return data_de_nascimento;
    }

    public void setData_de_nascimento(LocalDate data_de_nascimento) {
        this.data_de_nascimento = data_de_nascimento;
    }
}