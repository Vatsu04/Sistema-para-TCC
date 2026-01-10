package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.UserResponseDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Endpoint para retornar os dados do usuário atualmente autenticado.
     * @param userDetails Detalhes do usuário injetados pelo Spring Security a partir do token.
     * @return Os dados públicos do usuário em um DTO.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        // userDetails.getUsername() retorna o e-mail que foi definido no UserDetailsServiceImpl
        String userEmail = userDetails.getUsername();

        // Busca o usuário completo no banco de dados usando o e-mail
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco de dados, mas autenticado."));

        // Converte a entidade User para o DTO seguro e retorna
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    // Você pode adicionar outros endpoints aqui, como o de listar todos os usuários para um admin.
}