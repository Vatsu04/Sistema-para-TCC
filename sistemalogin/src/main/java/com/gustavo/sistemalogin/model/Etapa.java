package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "etapa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Funil getFunil() {
        return funil;
    }

    public void setFunil(Funil funil) {
        this.funil = funil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
// --- Relacionamentos ---

    // Muitas etapas pertencem a um único funil.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funil_id", nullable = false)
    @JsonIgnore // Evita loops de serialização ao buscar a etapa
    private Funil funil;
}
