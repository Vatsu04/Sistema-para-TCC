package com.gustavo.sistemalogin.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    @ManyToOne
    private User usuario;

    private String acao;

    private LocalDateTime data;

    private String detalhes;

    // Construtor vazio
    public AuditLog() {}

    // Construtor completo
    public AuditLog(User usuario, String acao, LocalDateTime data, String detalhes) {
        this.usuario = usuario;
        this.acao = acao;
        this.data = data;
        this.detalhes = detalhes;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }

    public String getAcao() { return acao; }
    public void setAcao(String acao) { this.acao = acao; }

    public LocalDateTime getData() { return data; }
    public void setData(LocalDateTime data) { this.data = data; }

    public String getDetalhes() { return detalhes; }
    public void setDetalhes(String detalhes) { this.detalhes = detalhes; }
}