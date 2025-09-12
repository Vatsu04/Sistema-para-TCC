package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Negocio;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NegocioResponseDTO {

    private Long id;
    private String titulo;
    private int pipeline_stage;
    private int id_ornizacao;
    private String etapa;

    // Dados "achatados" das entidades relacionadas
    private Long pessoaId;
    private String pessoaNome;
    private Long funilId;
    private String funilNome;

    public NegocioResponseDTO(Negocio negocio) {
        this.id = negocio.getId();
        this.titulo = negocio.getTitulo();
        this.pipeline_stage = negocio.getPipeline_stage();
        this.id_ornizacao = negocio.getId_ornizacao();
        this.etapa = negocio.getEtapa();

        if (negocio.getPessoa() != null) {
            this.pessoaId = negocio.getPessoa().getId();
            this.pessoaNome = negocio.getPessoa().getNome();
        }

        if (negocio.getFunil() != null) {
            this.funilId = negocio.getFunil().getId();
            this.funilNome = negocio.getFunil().getNome();
        }
    }
}
