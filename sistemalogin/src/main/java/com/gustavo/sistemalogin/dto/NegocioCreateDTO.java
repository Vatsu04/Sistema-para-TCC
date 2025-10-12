package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDate;

public class NegocioCreateDTO {

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    @NotBlank(message = "A etapa do negócio é obrigatória.")
    private String etapa;

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser positivo.")
    private BigDecimal valor;

    private LocalDate data_de_fechamento;

    @NotNull(message = "O ID do funil é obrigatório.")
    private Long funilId;

    @NotNull(message = "O ID da pessoa (contato) é obrigatório.")
    private Long pessoaId;

    @NotNull(message = "O ID da organização é obrigatório.")
    private Long organizacaoId;

    // Getters e Setters
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getEtapa() { return etapa; }
    public void setEtapa(String etapa) { this.etapa = etapa; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getData_de_fechamento() { return data_de_fechamento; }
    public void setData_de_fechamento(LocalDate data_de_fechamento) { this.data_de_fechamento = data_de_fechamento; }
    public Long getFunilId() { return funilId; }
    public void setFunilId(Long funilId) { this.funilId = funilId; }
    public Long getPessoaId() { return pessoaId; }
    public void setPessoaId(Long pessoaId) { this.pessoaId = pessoaId; }
    public Long getOrganizacaoId() { return organizacaoId; }
    public void setOrganizacaoId(Long organizacaoId) { this.organizacaoId = organizacaoId; }
}