package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.UserDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User cadastrarUsuario(UserDTO userDTO) {
        // Verifica se o e-mail já existe
        if(userRepository.findByEmail(userDTO.getEmail()).isPresent()) {
            throw new RuntimeException("E-mail já cadastrado!");
        }

        // Cria novo usuário
        User user = new User();
        user.setNome(userDTO.getNome());
        user.setEmail(userDTO.getEmail());
        // Hash da senha
        user.setSenha(passwordEncoder.encode(userDTO.getSenha()));

        // Salva no banco
        return userRepository.save(user);
    }
}