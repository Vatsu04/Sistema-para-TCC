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


    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findByUserEmail(String email);
}