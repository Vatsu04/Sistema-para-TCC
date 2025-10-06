// caminho: src/main/java/com/gustavo/sistemalogin/model/User.java
package com.gustavo.sistemalogin.model;

import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String email;

    private String senha;

    private boolean ativo;

    private Integer perfil;

    // Método "getter" para o perfil que converte o Integer para Enum
    public PerfilUsuario getPerfil() {
        return PerfilUsuario.toEnum(this.perfil);
    }

    // Método "setter" para o perfil que converte o Enum para Integer
    public void setPerfil(PerfilUsuario perfil) {
        if (perfil != null) {
            this.perfil = perfil.getCod();
        }
    }
}