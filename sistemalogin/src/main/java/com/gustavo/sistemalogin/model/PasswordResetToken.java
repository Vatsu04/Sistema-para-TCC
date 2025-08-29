package com.gustavo.sistemalogin.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "password_reset_tokens") // É uma boa prática usar nomes de tabela no plural
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user; // A relação com o usuário já existe aqui

    // O campo de email foi removido para evitar redundância.
    // O email pode ser acessado via this.user.getEmail()

    @Column(nullable = false)
    private LocalDateTime expirationDate;

    @Column(nullable = false)
    private boolean used = false;

    // Construtor vazio (requerido pelo JPA)
    public PasswordResetToken() {}

    // Construtor ajustado para receber o objeto User
    public PasswordResetToken(String token, User user, LocalDateTime expirationDate) {
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
        this.used = false;
    }

    public PasswordResetToken(String token, String email, LocalDateTime expirationDate) {
    }

    // Getters e Setters (get/set para email foram removidos)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }
}