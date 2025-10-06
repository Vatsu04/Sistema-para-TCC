package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.Organizacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrganizacaoRepository extends JpaRepository<Organizacao, Long> {
    List<Organizacao> findByUserEmail(String email);
}
