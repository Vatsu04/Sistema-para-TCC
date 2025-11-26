package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Funil;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@Data
public class FunilResponseDTO {

    private Long id;
    private String nome;
    private List<NegocioResponseDTO> negocios;

    public FunilResponseDTO(Funil funil) {
        this.id = funil.getId();
        this.nome = funil.getNome();
    }
}

