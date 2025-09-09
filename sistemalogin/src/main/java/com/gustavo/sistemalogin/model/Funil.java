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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;



    // --- Relacionamentos ---

    // Muitos funis podem pertencer a um usuário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore // Evita que os dados do usuário venham junto com o funil, prevenindo loops
    private User user;

    // Um funil pode ter uma lista de negócios.
    // 'mappedBy' indica que o lado 'Negocio' é o dono do relacionamento.
    // 'cascade = CascadeType.ALL' significa que se um funil for apagado, todos os negócios dentro dele também serão.
    @OneToMany(mappedBy = "funil", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Negocio> negocios;
}
