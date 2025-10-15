package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Etapa;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class EtapaResponseDTO {

    private Long id;
    private String nome;
    private int posicao;
    private Long funilId;

    public EtapaResponseDTO(Etapa etapa) {
        this.id = etapa.getId();
        this.nome = etapa.getNome();
        this.posicao = etapa.getPosicao();
        this.funilId = etapa.getFunil().getId();
    }
}