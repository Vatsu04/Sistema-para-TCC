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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService; // Adicionada a dependência do UserService

    /**
     * Faz login e retorna JWT
     */
    public String login(String email, String senha) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
        );
        // Após a autenticação bem-sucedida, buscamos o objeto User para gerar o token
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado após autenticação."));
        return tokenService.gerarToken(user);
    }

    /**
     * Logout (apenas client-side, pois JWT é stateless)
     */
    public void logout(String token) {
        // Em JWT stateless, o logout é feito removendo o token do cliente.
        // Se quiser blacklist, precisa implementar persistência de tokens inválidos.
    }

    /**
     * Registro de novo usuário
     */
    public User register(UserCreateDTO userData) {
        if (userRepository.findByEmail(userData.getEmail()).isPresent()) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }
        User newUser = new User();
        newUser.setNome(userData.getNome());
        newUser.setEmail(userData.getEmail());
        newUser.setSenha(passwordEncoder.encode(userData.getSenha()));
        newUser.setActive(true);
        // Definir um perfil padrão, por exemplo
        newUser.setPerfil(1);
        return userRepository.save(newUser);
    }

    /**
     * Valida o token JWT
     */
    public boolean validateToken(String token) {
        // 1. Extrai o email (subject) do token
        String email = tokenService.getSubject(token);
        if (email == null) {
            return false;
        }
        // 2. Carrega os detalhes do usuário a partir do email
        UserDetails userDetails = userService.loadUserByUsername(email);
        // 3. Valida o token contra os detalhes do usuário
        return tokenService.validateToken(token, userDetails);
    }

    /**
     * Recupera usuário pelo token JWT
     */
    public User getCurrentUser(String token) {
        String email = tokenService.getSubject(token);
        return userRepository.findByEmail(email).orElse(null);
    }
}
