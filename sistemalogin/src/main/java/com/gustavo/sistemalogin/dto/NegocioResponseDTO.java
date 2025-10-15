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
    private LocalDate dataDeAbertura;
    private LocalDate dataDeFechamento;
    private String organizacao;

    private Long etapaId;
    private String etapaNome;
    private Long funilId;
    private String funilNome;
    private Long pessoaId;
    private String pessoaNome;


    public NegocioResponseDTO(Negocio negocio) {
        this.id = negocio.getId();
        this.titulo = negocio.getTitulo();
        this.valor = negocio.getValor();
        this.dataDeAbertura = negocio.getDataDeAbertura();
        this.dataDeFechamento = negocio.getDataDeFechamento();
        this.organizacao = negocio.getOrganizacao();

        if (negocio.getEtapa() != null) {
            this.etapaId = negocio.getEtapa().getId();
            this.etapaNome = negocio.getEtapa().getNome();
        }
        if (negocio.getFunil() != null) {
            this.funilId = negocio.getFunil().getId();
            this.funilNome = negocio.getFunil().getNome();
        }
        if (negocio.getPessoa() != null) {
            this.pessoaId = negocio.getPessoa().getId();
            this.pessoaNome = negocio.getPessoa().getNome();
        }

    }
}