package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.LoginDTO;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController // Indica que esta classe é um controlador REST do Spring
@RequestMapping("/usuarios") // Prefixa todas as rotas deste controlador com "/usuarios"
public class UserController {

    // Injeta o repositório de usuários para acessar dados do banco
    @Autowired
    private UserRepository userRepository;

    // Injeta a classe utilitária para geração e validação do JWT
    @Autowired
    private JwtUtil jwtUtil;

    // Injeta o encoder de senha para comparar senhas criptografadas
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Endpoint de login: recebe email e senha, valida o usuário e retorna um JWT.
     *
     * @param loginDTO Objeto com os dados de login (email e senha)
     * @return ResponseEntity com o token JWT ou erro 401 se o login falhar
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO) {
        // Busca o usuário pelo email. O método retorna um Optional<User>.
        Optional<User> userOpt = userRepository.findByEmail(loginDTO.getEmail());

        // Valida se o usuário existe e se a senha está correta
        if (userOpt.isEmpty() || !passwordEncoder.matches(loginDTO.getSenha(), userOpt.get().getSenha())) {
            // Se não existir ou a senha estiver incorreta, retorna erro 401 (unauthorized)
            return ResponseEntity.status(401).body("Email ou senha inválidos");
        }

        // Se o usuário existe e a senha está correta, gera o token JWT usando o email
        User user = userOpt.get();
        String token = jwtUtil.generateToken(user.getEmail());

        // Retorna o token JWT no corpo da resposta (formato JSON)
        return ResponseEntity.ok().body("{\"token\":\"" + token + "\"}");
    }
}