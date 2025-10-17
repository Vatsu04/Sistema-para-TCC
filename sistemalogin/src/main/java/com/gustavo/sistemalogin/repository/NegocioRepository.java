package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NegocioRepository extends JpaRepository<Negocio, Long> {
    // Busca todos os negócios dentro de um funil específico
    List<Negocio> findByFunilId(Long funilId);


    List<Negocio> findByFunilUserEmail(String username);
    List<Negocio> findByFunil_User_Email(String email);
}
