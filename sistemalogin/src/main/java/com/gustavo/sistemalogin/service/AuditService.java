package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.model.AuditLog;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.AuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AuditService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    /**
     * Registra uma ação de auditoria
     */
    public void registerAudit(User usuario, String acao, String detalhes) {
        AuditLog audit = new AuditLog(usuario, acao, LocalDateTime.now(), detalhes);
        auditLogRepository.save(audit);
    }

    /**
     * Lista logs de auditoria, pode receber um filtro (exemplo: por usuário)
     */
    public List<AuditLog> listAudits(User usuario) {
        if (usuario != null) {
            return auditLogRepository.findByUsuario(usuario);
        } else {
            return auditLogRepository.findAll();
        }
    }
}