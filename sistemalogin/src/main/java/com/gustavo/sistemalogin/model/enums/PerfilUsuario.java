package com.gustavo.sistemalogin.model.enums;

import lombok.Getter;

@Getter
public enum PerfilUsuario {
    ADMINISTRADOR(1, "ROLE_ADMINISTRADOR"),
    ASSISTENTE(2, "ROLE_ASSISTENTE");

    private final int cod;
    private final String descricao;

    PerfilUsuario(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public static PerfilUsuario toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (PerfilUsuario x : PerfilUsuario.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Código de perfil inválido: " + cod);
    }
}