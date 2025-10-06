package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Organizacao;
import lombok.Data;

@Data
public class OrganizacaoResponseDTO{

    private Long Id;

    private String Email;

    private String Nome;

    private String Telefone;

    private String cnpj;

    private String endereco;

    public OrganizacaoResponseDTO(Organizacao organizacao) {
        this.Id = organizacao.getId();
        this.Nome = organizacao.getNome();
        this.cnpj = organizacao.getCnpj();
        this.Telefone = organizacao.getTelefone();
        this.Email = organizacao.getEmail();
        this.endereco = organizacao.getEndereco();
    }
}
