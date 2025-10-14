package com.gustavo.sistemalogin.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pessoas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Pessoa extends BaseEntity{

    @Column(unique = true) // CPF deve ser único
    private String cpf;




    private String rg;

    @Column(name = "data_de_nascimento")
    private LocalDate data_de_nascimento;

    // --- Relacionamentos ---

    // Muitas pessoas (contatos) podem pertencer a um usuário.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;

}
