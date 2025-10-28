package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.model.PasswordResetToken;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.PasswordResetTokenRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public PasswordResetToken createTokenForUser(String userEmail) {

        Optional<User> userOptional = userRepository.findByEmail(userEmail);

        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("Não foi encontrado um usuário com o email: " + userEmail);
        }
        User user = userOptional.get();

        Optional<PasswordResetToken> existingToken = tokenRepository.findByUserEmail(userEmail);

        if (existingToken.isPresent()) {
            tokenRepository.delete(existingToken.get());
        }

        String tokenString = UUID.randomUUID().toString();

        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);

        PasswordResetToken resetToken = new PasswordResetToken(tokenString, user, expirationDate);

        return tokenRepository.save(resetToken);
    }

    public Optional<PasswordResetToken> validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> passToken = tokenRepository.findByToken(token);

        if (passToken.isEmpty() || passToken.get().isUsed() || passToken.get().getExpirationDate().isBefore(LocalDateTime.now())) {
            return Optional.empty(); // Token inválido, usado ou expirado
        }

        return passToken;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        PasswordResetToken resetToken = validatePasswordResetToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido ou expirado."));

        User user = resetToken.getUser();
        if (user == null) {
            throw new RuntimeException("Token não está associado a nenhum usuário.");
        }

        user.setSenha(passwordEncoder.encode(newPassword));

        resetToken.setUsed(true);

        userRepository.save(user);
        tokenRepository.save(resetToken);
    }
}