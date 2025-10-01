package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.BaseEntity;
import com.gustavo.sistemalogin.model.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponseDTO extends BaseEntity {


    private String cpf;
    private String rg;
    private LocalDate data_de_nascimento;

    // Construtor de conveniência para mapear da Entidade para o DTO
    public PessoaResponseDTO(Pessoa pessoa) {
        this.setId(pessoa.getId());
        this.setNome(pessoa.getNome());
        this.setEmail(pessoa.getEmail());
        this.setTelefone(pessoa.getTelefone());
        this.cpf = pessoa.getCpf();
        this.rg = pessoa.getRg();
        this.data_de_nascimento = pessoa.getData_de_nascimento();
    }
}
