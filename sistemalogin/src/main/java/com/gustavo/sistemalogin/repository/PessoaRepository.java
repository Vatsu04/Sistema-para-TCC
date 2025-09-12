package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    // Busca todas as pessoas (contatos) de um usuário específico pelo ID do usuário
    List<Pessoa> findByUserId(Long userId);

    List<Pessoa>  findByUserEmail(String email);

}
