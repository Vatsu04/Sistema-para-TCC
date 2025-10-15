package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class NegocioCreateDTO {

    @NotBlank(message = "O título é obrigatório.")
    private String titulo; // Corrigido de 'nome' para 'titulo'

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser positivo.")
    private BigDecimal valor;

    private LocalDate data_de_fechamento;

    @NotNull(message = "O ID da etapa é obrigatório.")
    private Long etapaId; // Corrigido de 'etapa' para 'etapaId'

    @NotNull(message = "O ID do funil é obrigatório.")
    private Long funilId;

    @NotNull(message = "O ID da pessoa (contato) é obrigatório.")
    private Long pessoaId;

    @NotNull(message = "O ID da organização é obrigatório.")
    private Long organizacaoId;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Long getEtapaId() {
        return etapaId;
    }

    public void setEtapaId(Long etapaId) {
        this.etapaId = etapaId;
    }

    public LocalDate getData_de_fechamento() {
        return data_de_fechamento;
    }

    public void setData_de_fechamento(LocalDate data_de_fechamento) {
        this.data_de_fechamento = data_de_fechamento;
    }

    public Long getFunilId() {
        return funilId;
    }

    public void setFunilId(Long funilId) {
        this.funilId = funilId;
    }

    public Long getPessoaId() {
        return pessoaId;
    }

    public void setPessoaId(Long pessoaId) {
        this.pessoaId = pessoaId;
    }

    public Long getOrganizacaoId() {
        return organizacaoId;
    }

    public void setOrganizacaoId(Long organizacaoId) {
        this.organizacaoId = organizacaoId;
    }
}