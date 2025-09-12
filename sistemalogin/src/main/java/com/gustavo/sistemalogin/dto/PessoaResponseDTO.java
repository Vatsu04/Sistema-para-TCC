package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Pessoa;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private String cpf;
    private String rg;
    private LocalDate data_de_nascimento;

    // Construtor de conveniência para mapear da Entidade para o DTO
    public PessoaResponseDTO(Pessoa pessoa) {
        this.id = pessoa.getId();
        this.nome = pessoa.getNome();
        this.email = pessoa.getEmail();
        this.telefone = pessoa.getTelefone();
        this.cpf = pessoa.getCpf();
        this.rg = pessoa.getRg();
        this.data_de_nascimento = pessoa.getData_de_nascimento();
    }
}
