package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Etapa;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para enviar os dados de uma Etapa como resposta da API.
 * Ele formata os dados da entidade Etapa de forma segura e eficiente para o cliente.
 */
@Data
@NoArgsConstructor // O Lombok gera o construtor sem argumentos
public class EtapaResponseDTO {

    private Long id;
    private String nome;
    private Long funilId; // Apenas o ID do funil, para evitar enviar o objeto inteiro

    /**
     * Construtor que converte uma entidade Etapa em um EtapaResponseDTO.
     * Facilita a transformação dos dados na camada de serviço.
     *
     * @param etapa A entidade Etapa a ser convertida.
     */
    public EtapaResponseDTO(Etapa etapa) {
        this.id = etapa.getId();
        this.nome = etapa.getNome();
        this.funilId = etapa.getFunil().getId();
    }
}
