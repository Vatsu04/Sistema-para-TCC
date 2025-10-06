package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.Etapa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;


public interface EtapaRepository extends JpaRepository<Etapa, Long> {
    List<Etapa> findByFunilId(Long id);
}
