package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Organizacao;
import lombok.Data;

@Data
public class OrganizacaoResponseDTO {
    private Long id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String email;
    private String endereco;

    public OrganizacaoResponseDTO(Organizacao organizacao) {
        this.id = organizacao.getId();
        this.nome = organizacao.getNome();
        this.cnpj = organizacao.getCnpj();
        this.telefone = organizacao.getTelefone();
        this.email = organizacao.getEmail();
        this.endereco = organizacao.getEndereco();
    }
}
