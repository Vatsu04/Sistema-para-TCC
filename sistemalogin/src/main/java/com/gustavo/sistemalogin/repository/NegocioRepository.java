package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NegocioRepository extends JpaRepository<Negocio, Long> {
    // Busca todos os negócios dentro de um funil específico
    List<Negocio> findByFunilId(Long funilId);

    // Busca todos os negócios cujo funil pertence a um usuário específico (pelo email)
    // O Spring Data JPA entende a navegação: Negocio -> Funil -> User -> Email
    List<Negocio> findByFunilUserEmail(String email);

    void deleteByEtapaId(Long etapaId);

    void deleteByFunilId(Long funilId);


}