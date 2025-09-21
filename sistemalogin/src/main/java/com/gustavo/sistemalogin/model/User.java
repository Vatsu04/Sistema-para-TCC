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
        private String email;
        private String senha;

        // VERIFIQUE ESTE NOME! Pode ser 'active' em vez de 'ativo'
        @Column(name = "ativo") // A anotação @Column diz qual a coluna no banco
        private Boolean active; // Este é o nome que o JSON precisa usar

        // VERIFIQUE ESTA PARTE! O nome pode ser diferente ou pode ser um objeto
        @ManyToOne // Indica a relação Muitos-para-Um (muitos usuários para um perfil)
        @JoinColumn(name = "perfil_usuario") // Coluna que faz a junção
        private Integer perfil; // Se for um objeto, o JSON espera um objeto ou apenas o ID no campo 'perfil'

        public String getSenha() {
            return senha;
        }



        public Integer getPerfilUsuario() {
            return perfil;
        }

        public void setPerfilUsuario(int perfilUsuario) {
            perfil = perfilUsuario;
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

        public boolean isActive() {
            return active;
        }

        public void setActive(boolean ativo) {
            this.active = ativo;
        }
    }

