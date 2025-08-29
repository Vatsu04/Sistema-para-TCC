package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.model.PasswordResetToken;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.PasswordResetTokenRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    // Injeção de dependências dos repositórios necessários
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    /**
     * Cria um token de redefinição de senha para um usuário com base no email.
     * @param userEmail O email do usuário que solicitou a redefinição.
     * @return A entidade PasswordResetToken que foi criada e salva.
     * @throws UsernameNotFoundException se nenhum usuário for encontrado com o email fornecido.
     */
    public PasswordResetToken createTokenForUser(String userEmail) {

        // 1. Encontra o usuário no banco de dados pelo email
        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            // 2. Se o usuário não existir, lança uma exceção
            throw new UsernameNotFoundException("Não foi encontrado um usuário com o email: " + userEmail);
        }
        User user = userOptional.get();

        // 3. Gera uma string de token única e aleatória
        String tokenString = UUID.randomUUID().toString();

        // 4. Define a data de expiração do token (ex: válido por 1 hora a partir de agora)
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);

        // 5. Cria a entidade PasswordResetToken usando o construtor correto (que recebe o objeto User)
        PasswordResetToken resetToken = new PasswordResetToken(tokenString, user, expirationDate);

        // 6. Salva o novo token no banco de dados e o retorna
        return tokenRepository.save(resetToken);
    }

    /**
     * Valida um token de redefinição de senha.
     * @param token A string do token a ser validada.
     * @return O objeto PasswordResetToken se for válido, ou null se não for.
     */
    public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passToken = tokenRepository.findByToken(token);

        if (passToken.isEmpty() || passToken.get().isUsed() || passToken.get().getExpirationDate().isBefore(LocalDateTime.now())) {
            return Optional.empty(); // Token inválido, usado ou expirado
        }

        return passToken;
    }

    // Você pode adicionar outros métodos aqui, como um para efetivamente redefinir a senha.
}