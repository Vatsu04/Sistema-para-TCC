package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * Repositório para acessar e manipular os tokens de recuperação de senha no banco de dados.
 * Estende JpaRepository, que já fornece métodos prontos para CRUD.
 * Você pode adicionar métodos personalizados para buscar tokens por e-mail ou pelo próprio token.
 */
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {

    /**
     * Busca um token pelo valor do token (geralmente usado na etapa de validação de redefinição de senha).
     * @param token Valor do token (UUID)
     * @return Optional contendo o PasswordResetToken, se existir.
     */
    Optional<PasswordResetToken> findByToken(String token);

    /**
     * Busca um token pelo e-mail do usuário (útil para evitar múltiplos tokens ativos para o mesmo e-mail).
     * @param email E-mail do usuário
     * @return Optional contendo o PasswordResetToken, se existir.
     */
    Optional<PasswordResetToken> findByUserEmail(String email);
}