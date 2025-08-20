package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import com.gustavo.sistemalogin.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Faz login e retorna JWT
     */
    public String login(String email, String senha) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
        );
        User user = (User) authentication.getPrincipal();
        return jwtUtil.generateToken(user.getEmail());
    }

    /**
     * Logout (apenas client-side, pois JWT é stateless)
     */
    public void logout(String token) {
        // Em JWT stateless, o logout é feito removendo o token do client.
        // Se quiser blacklist, precisa implementar persistência de tokens inválidos.
    }

    /**
     * Registro de novo usuário
     */
    public User register(User userData) {
        if (userRepository.existsByEmail(userData.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }
        userData.setSenha(passwordEncoder.encode(userData.getSenha()));
        userData.setAtivo(true);
        return userRepository.save(userData);
    }

    /**
     * Valida o token JWT
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }

    /**
     * Recupera usuário pelo token JWT
     */
    public User getCurrentUser(String token) {
        String email = jwtUtil.extractUsername(token);
        return userRepository.findByEmail(email).orElse(null);
    }
}