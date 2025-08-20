package com.gustavo.sistemalogin.dto;

import java.math.BigDecimal;

/**
 * Data Transfer Object para Product.
 * Usado para transportar dados entre camadas sem expor a entidade completa.
 */
public class ProductDTO {
    private Long id;
    private String nome;
    private String descricao;
    private int quantidade;
    private String unidade;
    private BigDecimal valor;
    private String categoria;
    private boolean ativo;

    // Construtor vazio
    public ProductDTO() {}

    // Construtor completo
    public ProductDTO(Long id, String nome, String descricao, int quantidade, String unidade, BigDecimal valor, String categoria, boolean ativo) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.quantidade = quantidade;
        this.unidade = unidade;
        this.valor = valor;
        this.categoria = categoria;
        this.ativo = ativo;
    }

    // Construtor baseado em Product (opcional)
    public ProductDTO(com.gustavo.sistemalogin.model.Product product) {
        this.id = product.getId();
        this.nome = product.getNome();
        this.descricao = product.getDescricao();
        this.quantidade = product.getQuantidade();
        this.unidade = product.getUnidade();
        this.valor = product.getValor();
        this.categoria = product.getCategoria();
        this.ativo = product.isAtivo();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}