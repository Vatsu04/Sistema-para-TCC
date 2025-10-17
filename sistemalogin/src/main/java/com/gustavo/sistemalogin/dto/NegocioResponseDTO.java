package com.gustavo.sistemalogin.dto;

import com.gustavo.sistemalogin.model.Negocio;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
public class NegocioResponseDTO {
    private Long id;
    private String titulo;
    private BigDecimal valor;
    private LocalDate dataDeAbertura;
    private LocalDate dataDeFechamento;
    private String organizacao;

    // Campos de relacionamento (Funil e Etapa)
    private Long etapaId;
    private String etapaNome;
    private Long funilId;
    private String funilNome;

    // --- CAMPOS DO CONTATO (AGORA VINDOS DIRETAMENTE DE NEGOCIO) ---
    private String pessoaNome; // Mapeado de 'pessoaContato'
    private String emailPessoaContato;
    private String telefonePessoaContato;


    // O construtor agora usa os campos de String da própria entidade Negocio
    public NegocioResponseDTO(Negocio negocio) {
        this.id = negocio.getId();
        this.titulo = negocio.getTitulo();
        this.valor = negocio.getValor();
        this.dataDeAbertura = negocio.getDataDeAbertura();
        this.dataDeFechamento = negocio.getDataDeFechamento();
        this.organizacao = negocio.getOrganizacao();

        if (negocio.getEtapa() != null) {
            this.etapaId = negocio.getEtapa().getId();
            this.etapaNome = negocio.getEtapa().getNome();
        }
        if (negocio.getFunil() != null) {
            this.funilId = negocio.getFunil().getId();
            this.funilNome = negocio.getFunil().getNome();
        }

        // --- MAPEAMENTO CORRIGIDO ---
        // Removemos o 'pessoaId' pois não há mais uma entidade para referenciar.
        // Mapeamos diretamente os campos de texto.
        this.pessoaNome = negocio.getPessoaContato();
        this.emailPessoaContato = negocio.getEmailPessoaContato();
        this.telefonePessoaContato = negocio.getTelefonePessoaContato();
    }
}