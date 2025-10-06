package com.gustavo.sistemalogin.model;

import com.gustavo.sistemalogin.model.enums.PerfilUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private boolean ativo;

    @Column(name = "perfil")
    private Integer perfil;

    public PerfilUsuario getPerfil() {
        return PerfilUsuario.toEnum(this.perfil);
    }

    public void setPerfil(PerfilUsuario perfil) {
        if (perfil != null) {
            this.perfil = perfil.getCod();
        }
    }
}