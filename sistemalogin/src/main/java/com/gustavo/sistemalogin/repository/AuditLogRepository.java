package com.gustavo.sistemalogin.repository;

import com.gustavo.sistemalogin.model.AuditLog;
import com.gustavo.sistemalogin.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {


    List<AuditLog> findByUsuario(User usuario);


}