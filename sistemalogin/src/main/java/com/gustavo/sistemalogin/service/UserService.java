package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.UserCreateDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.gustavo.sistemalogin.dto.UserResponseDTO; // Adicione este import
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors; // Adicione este import
import java.util.List; // Adicione este import


@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Cria um novo utilizador com um perfil padrão.
     */
    public User createUser(UserCreateDTO userCreateDTO) {
        if (userRepository.existsByEmail(userCreateDTO.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }

        User newUser = new User();
        newUser.setNome(userCreateDTO.getNome());
        newUser.setEmail(userCreateDTO.getEmail());
        newUser.setSenha(passwordEncoder.encode(userCreateDTO.getSenha()));
        newUser.setAtivo(true); // Define o utilizador como ativo por defeito

        // --- LÓGICA CORRIGIDA ---
        // Atribui o perfil padrão (ASSISTENTE) diretamente no servidor.
        newUser.setPerfil(PerfilUsuario.ASSISTENTE);

        return userRepository.save(newUser);
    }
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

}