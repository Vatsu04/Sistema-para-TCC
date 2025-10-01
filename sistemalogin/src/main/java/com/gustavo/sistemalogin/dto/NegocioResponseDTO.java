package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.BaseEntity;
import com.gustavo.sistemalogin.model.Negocio;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para enviar os dados de um Negócio como resposta da API.
 * Ele "achata" a estrutura, enviando apenas os dados necessários.
 */
@Data
public class NegocioResponseDTO extends BaseEntity {
    private String titulo;
    private String etapa;
    private BigDecimal valor;
    private Long funilId;
    private String funilNome;
    private Long pessoaId;
    private String pessoaNome;

    /**
     * Construtor que converte uma entidade Negocio para este DTO.
     * É aqui que a "magia" da conversão acontece.
     * @param negocio A entidade vinda do banco de dados.
     */
    public NegocioResponseDTO(Negocio negocio) {
        this.setId(negocio.getId());
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

