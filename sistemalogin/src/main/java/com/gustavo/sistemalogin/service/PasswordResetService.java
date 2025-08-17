package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.model.PasswordResetToken;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.PasswordResetTokenRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Serviço responsável pelo fluxo de recuperação de senha:
 * - geração e envio de token por e-mail
 * - validação do token
 * - redefinição segura da senha do usuário
 */
@Service
public class PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Solicita recuperação de senha para um e-mail cadastrado.
     * Gera token, salva no banco e envia e-mail ao usuário.
     *
     * @param email E-mail do usuário que deseja recuperar a senha.
     */
    public void requestPasswordReset(String email) {
        // Verifica se existe usuário com o e-mail informado
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            // Não revela se o e-mail existe ou não para segurança (não informa ao usuário)
            return;
        }

        // Gera token único e define validade de 1 hora
        String token = UUID.randomUUID().toString();
        LocalDateTime expirationDate = LocalDateTime.now().plusHours(1);

        // Cria objeto token e salva no banco
        PasswordResetToken prt = new PasswordResetToken(token, email, expirationDate);
        prt.setUsed(false);
        tokenRepository.save(prt);

        // Monta link de recuperação (ajuste domínio conforme seu frontend)
        String resetLink = "https://seusite.com/reset-password?token=" + token;

        // Envia e-mail ao usuário
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Recuperação de senha");
        message.setText("Clique no link para redefinir sua senha: " + resetLink);
        mailSender.send(message);
    }

    /**
     * Valida o token de recuperação recebido.
     * Verifica se o token existe, não expirou e não foi utilizado.
     *
     * @param token Token recebido por e-mail
     * @return PasswordResetToken válido ou null se inválido/expirado
     */
    public PasswordResetToken validatePasswordResetToken(String token) {
        Optional<PasswordResetToken> prtOpt = tokenRepository.findByToken(token);
        if (prtOpt.isEmpty()) return null;

        PasswordResetToken prt = prtOpt.get();
        // Confere se já foi usado ou expirou
        if (prt.isUsed() || prt.getExpirationDate().isBefore(LocalDateTime.now())) {
            return null;
        }
        return prt;
    }

    /**
     * Redefine a senha do usuário usando o token de recuperação.
     * Criptografa a nova senha com BCrypt e marca o token como usado.
     *
     * @param token Token de recuperação válido
     * @param newPassword Nova senha em texto puro
     * @return true se a senha foi redefinida com sucesso, false caso contrário
     */
    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken prt = validatePasswordResetToken(token);
        if (prt == null) return false;

        Optional<User> userOpt = userRepository.findByEmail(prt.getEmail());
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        // Criptografa nova senha
        user.setSenha(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Marca token como usado para garantir segurança
        prt.setUsed(true);
        tokenRepository.save(prt);

        return true;
    }
}