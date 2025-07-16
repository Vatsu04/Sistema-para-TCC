package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.LoginDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import com.gustavo.sistemalogin.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        User user = userRepository.findByEmail(loginDTO.getEmail());
        if (user == null || !passwordEncoder.matches(loginDTO.getSenha(), user.getSenha())) {
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
    }
}