package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.AdminUserCreateDTO;
import com.gustavo.sistemalogin.dto.AdminUserUpdateDTO;
import com.gustavo.sistemalogin.dto.UserResponseDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import com.gustavo.sistemalogin.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.gustavo.sistemalogin.dto.UserSelfUpdateDTO;


import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository; // Mantido para o /me
    private final UserService userService; // Serviço com a lógica de admin

    public UserController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    /**
     * Endpoint público (apenas autenticado) para retornar os dados do usuário logado.
     */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        String userEmail = userDetails.getUsername();
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário autenticado não encontrado no banco."));
        return ResponseEntity.ok(new UserResponseDTO(user));
    }

    // --- ENDPOINTS EXCLUSIVOS PARA ADMINISTRADORES ---

    /**
     * [ADMIN] Lista TODOS os usuários do sistema.
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.findAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * [ADMIN] Cria um novo usuário com perfil e status específicos.
     */
    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserResponseDTO> createUserAsAdmin(@Valid @RequestBody AdminUserCreateDTO dto) {
        User newUser = userService.createUserAsAdmin(dto);
        return new ResponseEntity<>(new UserResponseDTO(newUser), HttpStatus.CREATED);
    }

    /**
     * [ADMIN] Atualiza os dados de um usuário existente.
     */
    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserResponseDTO> updateUserAsAdmin(@PathVariable Long id, @Valid @RequestBody AdminUserUpdateDTO dto) {
        UserResponseDTO updatedUser = userService.updateUserAsAdmin(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    /**
     * [ADMIN] Deleta um usuário pelo ID.
     */
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<Void> deleteUserAsAdmin(@PathVariable Long id) {
        userService.deleteUserAsAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/me") // Usa PUT na rota /me
    public ResponseEntity<UserResponseDTO> updateMyProfile(
            @Valid @RequestBody UserSelfUpdateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        UserResponseDTO updatedUser = userService.updateMyProfile(userDetails.getUsername(), dto);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMINISTRADOR')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    

}