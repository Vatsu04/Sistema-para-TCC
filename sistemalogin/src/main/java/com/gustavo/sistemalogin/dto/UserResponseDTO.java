package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import lombok.Data;

/**
 * DTO para enviar os dados de um Utilizador como resposta da API.
 * NUNCA expõe a senha ou outros dados sensíveis.
 */
@Data
public class UserResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private boolean ativo;
    private PerfilUsuario perfil;

    /**
     * Construtor que converte uma entidade User para este DTO.
     * @param user A entidade User vinda da base de dados.
     */
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.nome = user.getNome();
        this.email = user.getEmail();
        this.ativo = user.isAtivo();
        this.perfil = user.getPerfil();
    }
}