package com.gustavo.sistemalogin.dto;

import jakarta.validation.constraints.Email;
import java.math.BigDecimal;
import java.time.LocalDate;

public class NegocioUpdateDTO {

    private String titulo;
    private BigDecimal valor;
    private LocalDate data_de_fechamento;
    private Long etapaId;
    private String organizacao;

    // Campos de contato opcionais para atualização
    private String pessoaContato;
    @Email(message = "O formato do e-mail do contato é inválido.")
    private String emailPessoaContato;
    private String telefonePessoaContato;


    private LocalDate dataDeAbertura;



    public LocalDate getDataDeAbertura() {
        return dataDeAbertura;
    }

    public void setDataDeAbertura(LocalDate dataDeAbertura) {
        this.dataDeAbertura = dataDeAbertura;
    }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }
    public LocalDate getData_de_fechamento() { return data_de_fechamento; }
    public void setData_de_fechamento(LocalDate data_de_fechamento) { this.data_de_fechamento = data_de_fechamento; }
    public Long getEtapaId() { return etapaId; }
    public void setEtapaId(Long etapaId) { this.etapaId = etapaId; }
    public String getOrganizacao() { return organizacao; }
    public void setOrganizacao(String organizacao) { this.organizacao = organizacao; }
    public String getPessoaContato() { return pessoaContato; }
    public void setPessoaContato(String pessoaContato) { this.pessoaContato = pessoaContato; }
    public String getEmailPessoaContato() { return emailPessoaContato; }
    public void setEmailPessoaContato(String emailPessoaContato) { this.emailPessoaContato = emailPessoaContato; }
    public String getTelefonePessoaContato() { return telefonePessoaContato; }
    public void setTelefonePessoaContato(String telefonePessoaContato) { this.telefonePessoaContato = telefonePessoaContato; }
}