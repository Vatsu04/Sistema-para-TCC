package com.gustavo.sistemalogin.model.enums;

public enum PerfilUsuario {
    ADMINISTRADOR(1, "Perfil de Administrador do Sistema"),
    ASSISTENTE(2, "Perfil de Assistente com acesso limitado");

    private int cod;
    private String descricao;

    private PerfilUsuario(int cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public int getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
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
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
