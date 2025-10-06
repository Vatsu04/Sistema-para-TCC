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
    private PerfilUsuario perfil; // Enviamos a descrição do perfil, que é mais útil para o front-end

    /**
     * Construtor que converte uma entidade User para este DTO.
     * @param user A entidade User vinda da base de dados.
     */
    public UserResponseDTO(User user) {
        this.id = user.getId();
        this.nome = user.getNome();
        this.email = user.getEmail();
        this.ativo = user.isAtivo();
        // --- LÓGICA CORRIGIDA AQUI ---
        // 1. Obtém o código numérico do perfil do objeto User.
        PerfilUsuario perfilCod = user.getPerfil();

        // 2. Converte o código para o objeto Enum correspondente.
        PerfilUsuario perfilEnum = PerfilUsuario.toEnum(perfilCod);

        // 3. Obtém a descrição do Enum e atribui ao campo do DTO.
        //    (Verifica se não é nulo para segurança extra).
        if (perfilEnum != null) {
            user.setPerfil(PerfilUsuario.toEnum(perfilCod));
        } else {
            System.out.println("PERFIL INVÁLIDO");
        }
    }
}
