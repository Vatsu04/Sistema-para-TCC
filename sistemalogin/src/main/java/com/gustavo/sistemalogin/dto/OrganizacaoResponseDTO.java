package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.BaseEntity;
import com.gustavo.sistemalogin.model.Organizacao;
import lombok.Data;

@Data
public class OrganizacaoResponseDTO extends BaseEntity {

    private String cnpj;

    private String endereco;

    public OrganizacaoResponseDTO(Organizacao organizacao) {
        this.setId(organizacao.getId());
        this.setNome(organizacao.getNome());
        this.cnpj = organizacao.getCnpj();
        this.setTelefone(organizacao.getTelefone());
        this.setEmail(organizacao.getEmail());
        this.endereco = organizacao.getEndereco();
    }
}
