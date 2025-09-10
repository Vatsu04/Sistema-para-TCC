package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.Funil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FunilRepository extends JpaRepository<Funil, Long> {
    List<Funil> findByUserId(Long userId);
}
