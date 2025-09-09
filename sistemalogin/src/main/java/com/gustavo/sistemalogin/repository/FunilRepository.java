package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.Funil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunilRepository extends JpaRepository<Funil, Long> {
    // Busca todos os funis de um usuário específico pelo ID do usuário
    List<Funil> findByUserId(Long userId);
}
