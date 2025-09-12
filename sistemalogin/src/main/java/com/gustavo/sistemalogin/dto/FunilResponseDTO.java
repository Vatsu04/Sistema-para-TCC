package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Funil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FunilResponseDTO {

    private Long id;
    private String nome;

    public FunilResponseDTO(Funil funil) {
        this.id = funil.getId();
        this.nome = funil.getNome();
    }
}
