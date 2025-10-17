package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.UserCreateDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import com.gustavo.sistemalogin.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService; // Usado para a lógica de registo

    // --- MUDANÇA APLICADA AQUI ---
    @Autowired
    private UserDetailsServiceImpl userDetailsService; // Usado para a lógica de validação

    /**
     * Faz login e retorna JWT
     */
    public String login(String email, String senha) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
        );
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado após autenticação."));
        return tokenService.gerarToken(user);
    }

    /**
     * Registo de novo usuário
     */
    public User register(UserCreateDTO userData) {
        // A lógica de criação de usuário foi movida para o UserService
        return userService.createUser(userData);
    }

    /**
     * Valida o token JWT
     */
    public boolean validateToken(String token) {
        String email = tokenService.getSubject(token);
        if (email == null) {
            return false;
        }
        // --- MUDANÇA APLICADA AQUI ---
        // Agora usa o serviço correto para carregar os detalhes do usuário
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return tokenService.validateToken(token, userDetails);
    }

    /**
     * Recupera usuário pelo token JWT
     */
    public User getCurrentUser(String token) {
        String email = tokenService.getSubject(token);
        return userRepository.findByEmail(email).orElse(null);
    }

    // O método de logout pode ser mantido ou removido, pois é client-side
}