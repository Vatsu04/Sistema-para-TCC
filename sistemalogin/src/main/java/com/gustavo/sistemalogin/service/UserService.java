package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.AdminUserCreateDTO;
import com.gustavo.sistemalogin.dto.AdminUserUpdateDTO;
import com.gustavo.sistemalogin.dto.UserResponseDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.gustavo.sistemalogin.dto.UserSelfUpdateDTO;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * [ADMIN] Cria um novo usuário com perfil e status definidos pelo admin.
     * Substitui o antigo método público 'createUser'.
     */
    @Transactional
    public User createUserAsAdmin(AdminUserCreateDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado!");
        }

        User newUser = new User();
        newUser.setNome(dto.getNome());
        newUser.setEmail(dto.getEmail());
        newUser.setSenha(passwordEncoder.encode(dto.getSenha()));
        newUser.setAtivo(dto.getAtivo()); // Usa o status do DTO
        newUser.setPerfil(dto.getPerfil()); // Usa o perfil do DTO

        return userRepository.save(newUser);
    }

    /**
     * [ADMIN] Atualiza os dados de um usuário existente.
     */
    @Transactional
    public UserResponseDTO updateUserAsAdmin(Long userId, AdminUserUpdateDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário com ID " + userId + " não encontrado."));

        // Atualiza apenas os campos fornecidos no DTO
        if (dto.getNome() != null) user.setNome(dto.getNome());
        if (dto.getEmail() != null) {
            // Verifica se o novo email já não está em uso por OUTRO usuário
            userRepository.findByEmail(dto.getEmail()).ifPresent(existingUser -> {
                if (!existingUser.getId().equals(userId)) {
                    throw new IllegalArgumentException("Este e-mail já está em uso por outro usuário.");
                }
            });
            user.setEmail(dto.getEmail());
        }
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            // Só atualiza a senha se uma nova senha (válida) for fornecida
            user.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
        if (dto.getAtivo() != null) user.setAtivo(dto.getAtivo());
        if (dto.getPerfil() != null) user.setPerfil(dto.getPerfil());

        User updatedUser = userRepository.save(user);
        return new UserResponseDTO(updatedUser);
    }

    /**
     * [ADMIN] Deleta um usuário pelo ID.
     */
    @Transactional
    public void deleteUserAsAdmin(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("Usuário com ID " + userId + " não encontrado.");
        }
        // Adicionar validação extra aqui se necessário (ex: não permitir deletar a si mesmo)
        userRepository.deleteById(userId);
    }

    /**
     * [ADMIN] Retorna uma lista de todos os usuários do sistema.
     */
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserResponseDTO updateMyProfile(String userEmail, UserSelfUpdateDTO dto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado no banco."));

        // Atualiza o nome se foi fornecido no DTO
        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            user.setNome(dto.getNome().trim());
        }
        // No futuro, a lógica para alterar senha (com validação da senha atual) pode entrar aqui.

        User updatedUser = userRepository.save(user);
        return new UserResponseDTO(updatedUser);
    }


}