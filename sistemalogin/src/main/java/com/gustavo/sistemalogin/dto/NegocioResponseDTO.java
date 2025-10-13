package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Negocio;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class NegocioResponseDTO {
    private Long id;
    private String titulo;
    private BigDecimal valor;
    private String etapa;
    private LocalDate dataDeAbertura;
    private LocalDate dataDeFechamento;

    // Informações das entidades relacionadas
    private Long funilId;
    private String funilNome;
    private Long pessoaId;
    private String pessoaNome;
    private Long organizacaoId;
    private String organizacaoNome;

    public NegocioResponseDTO(Negocio negocio) {
        this.id = negocio.getId();
        this.titulo = negocio.getTitulo();
        this.valor = negocio.getValor();
        this.etapa = negocio.getEtapa();
        this.dataDeAbertura = negocio.getDataDeAbertura();
        this.dataDeFechamento = negocio.getData_de_fechamento();

        if (negocio.getFunil() != null) {
            this.funilId = negocio.getFunil().getId();
            this.funilNome = negocio.getFunil().getNome();
        }
        if (negocio.getPessoa() != null) {
            this.pessoaId = negocio.getPessoa().getId();
            this.pessoaNome = negocio.getPessoa().getNome();
        }
        if (negocio.getOrganizacao() != null) {
            this.organizacaoId = negocio.getOrganizacao().getId();
            this.organizacaoNome = negocio.getOrganizacao().getNome();
        }
    }
}