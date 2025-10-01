package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.BaseEntity;
import com.gustavo.sistemalogin.model.Negocio;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class NegocioResponseDTO {
    private Long id;
    private String titulo;
    private String etapa;
    private BigDecimal valor;
    private Long funilId;
    private String funilNome;
    private Long pessoaId;
    private String pessoaNome;

    public NegocioResponseDTO(Negocio negocio) {
        this.id = negocio.getId();
        this.titulo = negocio.getTitulo();
        this.etapa = negocio.getEtapa();
        this.valor = negocio.getValor();

        // Verifica se o funil não é nulo para evitar NullPointerException
        if (negocio.getFunil() != null) {
            this.funilId = negocio.getFunil().getId();
            this.funilNome = negocio.getFunil().getNome();
        }

        // Verifica se a pessoa não é nula
        if (negocio.getPessoa() != null) {
            this.pessoaId = negocio.getPessoa().getId();
            this.pessoaNome = negocio.getPessoa().getNome();
        }
    }

    public NegocioResponseDTO() {
    }

    public NegocioResponseDTO(Object o) {
    }
}

