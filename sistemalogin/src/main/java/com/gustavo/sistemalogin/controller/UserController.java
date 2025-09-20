package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.LoginDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import com.gustavo.sistemalogin.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // <-- IMPORT CORRETO
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users") // <-- AJUSTE 1: Rota corrigida
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder; // <-- AJUSTE 2: Usando a interface

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());

        if (userOpt.isEmpty() || !passwordEncoder.matches(loginDTO.getSenha(), userOpt.get().getSenha())) {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }

        User user = userOpt.get();
        String token = tokenService.generateToken(user.getEmail());

        return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
    }
}
