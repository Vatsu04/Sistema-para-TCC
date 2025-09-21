package com.gustavo.sistemalogin.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Data // Gera automaticamente getters, setters, toString, equals, hashCode
    @NoArgsConstructor // Gera o construtor vazio, essencial para o JPA
    @AllArgsConstructor // Gera um construtor com todos os campos
    @Entity
    @Table(name = "users")
    public class User {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nome;
        @Column(unique = true)
        private String email;
        private String senha;
        private Integer PerfilUsuario;
        private boolean ativo;

        public String getSenha() {
            return senha;
        }

        public int getPerfilUsuario() {
            return PerfilUsuario;
        }

        public void setPerfilUsuario(int perfilUsuario) {
            PerfilUsuario = perfilUsuario;
        }

        public void setSenha(String senha) {
            this.senha = senha;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public boolean isAtivo() {
            return ativo;
        }

        public void setAtivo(boolean ativo) {
            this.ativo = ativo;
        }
    }

