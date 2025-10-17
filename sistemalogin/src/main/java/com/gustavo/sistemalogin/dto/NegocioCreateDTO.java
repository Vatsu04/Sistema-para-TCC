package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class NegocioCreateDTO {

    @NotBlank(message = "O título é obrigatório.")
    private String titulo;

    @NotNull(message = "O valor é obrigatório.")
    @Positive(message = "O valor deve ser positivo.")
    private BigDecimal valor;

    private LocalDate data_de_fechamento;

    @NotNull(message = "O ID da etapa é obrigatório.")
    private Long etapaId;

    @NotNull(message = "O ID do funil é obrigatório.")
    private Long funilId;

    @NotBlank(message = "O nome da organização é obrigatório.")
    private String organizacao;

    // --- NOVOS CAMPOS PARA DADOS DO CONTATO ---
    @NotBlank(message = "O nome do contato é obrigatório.")
    private String pessoaContato;

    @NotBlank(message = "O email do contato é obrigatório.")
    @Email(message = "O formato do e-mail do contato é inválido.")
    private String emailPessoaContato;

    @NotBlank(message = "O telefone do contato é obrigatório.")
    private String telefonePessoaContato;

    // Getters e Setters para todos os campos...
    // (O campo pessoaId foi removido e substituído pelos três acima)

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getData_de_fechamento() { return data_de_fechamento; }
    public void setData_de_fechamento(LocalDate data_de_fechamento) { this.data_de_fechamento = data_de_fechamento; }
    public Long getEtapaId() { return etapaId; }
    public void setEtapaId(Long etapaId) { this.etapaId = etapaId; }
    public Long getFunilId() { return funilId; }
    public void setFunilId(Long funilId) { this.funilId = funilId; }
    public String getOrganizacao() { return organizacao; }
    public void setOrganizacao(String organizacao) { this.organizacao = organizacao; }
    public String getPessoaContato() { return pessoaContato; }
    public void setPessoaContato(String pessoaContato) { this.pessoaContato = pessoaContato; }
    public String getEmailPessoaContato() { return emailPessoaContato; }
    public void setEmailPessoaContato(String emailPessoaContato) { this.emailPessoaContato = emailPessoaContato; }
    public String getTelefonePessoaContato() { return telefonePessoaContato; }
    public void setTelefonePessoaContato(String telefonePessoaContato) { this.telefonePessoaContato = telefonePessoaContato; }
}