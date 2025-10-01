package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Funil;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO para enviar os dados de um Funil como resposta da API.
 * Este DTO não expõe as entidades completas, garantindo a segurança.
 */
@Data
@NoArgsConstructor // Lombok: cria um construtor sem argumentos
public class FunilResponseDTO {

    private Long id;
    private String nome;
    private List<NegocioResponseDTO> negocios;

    /**
     * Construtor que converte uma entidade Funil para este DTO.
     * Este é o construtor que resolve o erro de compilação no seu FunilService,
     * pois ele sabe como receber um objeto Funil.
     * @param funil A entidade Funil vinda da base de dados.
     */
    public FunilResponseDTO(Funil funil) {
        this.id = funil.getId();
        this.nome = funil.getNome();

        // Converte a lista de entidades Negocio para uma lista de NegocioResponseDTOs.
        // Isto é crucial para a segurança e para evitar erros de serialização.
        if (funil.getNegocios() != null) {
            this.negocios = funil.getNegocios().stream()
                    .map(NegocioResponseDTO::new) // Para cada Negocio, cria um NegocioResponseDTO
                    .collect(Collectors.toList());
        } else {
            this.negocios = Collections.emptyList(); // Retorna uma lista vazia se não houver negócios
        }
    }

    public FunilResponseDTO(Object o) {
    }
}

