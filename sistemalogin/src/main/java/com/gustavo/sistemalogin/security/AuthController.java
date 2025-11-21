package com.gustavo.sistemalogin.security;

import com.gustavo.sistemalogin.dto.LoginDTO;
import com.gustavo.sistemalogin.dto.AdminUserCreateDTO;
import com.gustavo.sistemalogin.model.PasswordResetToken;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import com.gustavo.sistemalogin.service.PasswordResetService;
import com.gustavo.sistemalogin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import jakarta.validation.Valid;
import com.gustavo.sistemalogin.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.gustavo.sistemalogin.dto.ForgotPasswordRequestDTO;
import com.gustavo.sistemalogin.dto.ResetPasswordRequestDTO;



@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService; // MUDANÇA AQUI
    private final UserRepository userRepository;

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private EmailService emailService;

    public AuthController(AuthenticationManager authenticationManager, UserService userService, TokenService tokenService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.tokenService = tokenService; // MUDANÇA AQUI
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@Valid @RequestBody AdminUserCreateDTO adminUserCreateDTO) {
        userService.createUserAsAdmin(adminUserCreateDTO);
        return ResponseEntity.ok("Usuário registrado com sucesso!");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getSenha())
        );

        User user = userRepository.findByEmail(loginDTO.getEmail()).orElseThrow();
        String token = tokenService.gerarToken(user);

        Map<String, String> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> handleForgotPassword(@RequestBody ForgotPasswordRequestDTO request) {
        try {

            PasswordResetToken token = passwordResetService.createTokenForUser(request.getEmail());


            String resetLink = "http://127.0.0.1:5500/sistemalogin/frontend/front%20end/mudarsenha.html?token=" + token.getToken();


            emailService.sendPasswordResetEmail(request.getEmail(), resetLink);

        } catch (UsernameNotFoundException e) {
            // Se o e-mail não for encontrado, NÃO informe o usuário (por segurança).
            // Apenas logamos no servidor.
            System.out.println("Solicitação de redefinição para e-mail não cadastrado: " + request.getEmail());
        } catch (Exception e) {

            System.err.println("Erro ao processar forgot-password: " + e.getMessage());
            return ResponseEntity.status(500).body("Erro interno no servidor.");
        }


        return ResponseEntity.ok("Solicitação recebida. Se o e-mail estiver cadastrado, um link será enviado.");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<String> validateToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        if (token == null) {
            return ResponseEntity.badRequest().body("Token não fornecido.");
        }

        if (passwordResetService.validatePasswordResetToken(token).isPresent()) {
            return ResponseEntity.ok("Token é válido.");
        } else {
            return ResponseEntity.badRequest().body("Token inválido ou expirado.");
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> handleResetPassword(@Valid @RequestBody ResetPasswordRequestDTO request) {

        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity.badRequest().body("As senhas não coincidem.");
        }

        try {
            passwordResetService.resetPassword(request.getToken(), request.getNewPassword());
            return ResponseEntity.ok("Senha alterada com sucesso!");

        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
