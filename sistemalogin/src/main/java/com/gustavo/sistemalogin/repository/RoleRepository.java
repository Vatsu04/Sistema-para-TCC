package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Método para buscar um perfil pelo nome (ex: "ADMINISTRADOR")
    Optional<Role> findByNome(String nome);
}