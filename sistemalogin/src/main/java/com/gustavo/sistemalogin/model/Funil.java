package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "funis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funil {

    @Id // <--- ESTAVA FALTANDO ISSO
    @GeneratedValue(strategy = GenerationType.IDENTITY) // <--- E ISSO PARA O AUTO INCREMENT
    private Long id;

    @Column(nullable = false)
    private String nome;

    // --- Relacionamentos ---

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;


    @OneToMany(mappedBy = "funil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Negocio> negocios;

}