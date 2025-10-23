package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.NegocioCreateDTO;
import com.gustavo.sistemalogin.dto.NegocioResponseDTO;
import com.gustavo.sistemalogin.dto.NegocioUpdateDTO;
import com.gustavo.sistemalogin.model.*;
import com.gustavo.sistemalogin.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NegocioService {

    private final NegocioRepository negocioRepository;
    private final FunilRepository funilRepository;
    private final EtapaRepository etapaRepository;
    // PessoaRepository e OrganizacaoRepository não são mais necessários aqui

    public NegocioService(NegocioRepository negocioRepository, FunilRepository funilRepository,
                          EtapaRepository etapaRepository) {
        this.negocioRepository = negocioRepository;
        this.funilRepository = funilRepository;
        this.etapaRepository = etapaRepository;
    }

    @Transactional
    public NegocioResponseDTO criarNegocio(NegocioCreateDTO dto, String userEmail) {
        // Validações de propriedade e consistência (Funil e Etapa)
        Funil funil = funilRepository.findById(dto.getFunilId())
                .filter(f -> f.getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new SecurityException("Funil não encontrado ou não pertence ao usuário."));

        Etapa etapa = etapaRepository.findById(dto.getEtapaId())
                .orElseThrow(() -> new RuntimeException("Etapa não encontrada."));

        if (!etapa.getFunil().getId().equals(funil.getId())) {
            throw new IllegalArgumentException("A etapa informada não pertence ao funil especificado.");
        }

        Negocio novoNegocio = new Negocio();
        novoNegocio.setTitulo(dto.getTitulo());
        novoNegocio.setValor(dto.getValor());

        // --- CORREÇÃO: Usa as datas do DTO ---
        novoNegocio.setDataDeAbertura(dto.getDataDeAbertura());
        novoNegocio.setDataDeFechamento(dto.getData_de_fechamento());

        novoNegocio.setOrganizacao(dto.getOrganizacao());
        novoNegocio.setEtapa(etapa);
        novoNegocio.setFunil(funil);

        // --- ATRIBUI OS DADOS DO CONTATO DIRETAMENTE ---
        novoNegocio.setPessoaContato(dto.getPessoaContato());
        novoNegocio.setEmailPessoaContato(dto.getEmailPessoaContato());
        novoNegocio.setTelefonePessoaContato(dto.getTelefonePessoaContato());

        Negocio negocioSalvo = negocioRepository.save(novoNegocio);
        return new NegocioResponseDTO(negocioSalvo);
    }

    @Transactional
    public NegocioResponseDTO atualizarNegocio(Long id, NegocioUpdateDTO updateDTO, String username) {
        Negocio negocio = negocioRepository.findById(id)
                .filter(n -> n.getFunil().getUser().getEmail().equals(username))
                .orElseThrow(() -> new SecurityException("Negócio não encontrado ou acesso negado."));

        // Atualiza campos normais
        if (updateDTO.getTitulo() != null) negocio.setTitulo(updateDTO.getTitulo());
        if (updateDTO.getValor() != null) negocio.setValor(updateDTO.getValor());
        // --- CORREÇÃO: Permite atualizar a data de abertura ---
        if (updateDTO.getDataDeAbertura() != null) negocio.setDataDeAbertura(updateDTO.getDataDeAbertura());
        if (updateDTO.getData_de_fechamento() != null) negocio.setDataDeFechamento(updateDTO.getData_de_fechamento());
        if (updateDTO.getOrganizacao() != null) negocio.setOrganizacao(updateDTO.getOrganizacao());

        // Atualiza campos do contato
        if (updateDTO.getPessoaContato() != null) negocio.setPessoaContato(updateDTO.getPessoaContato());
        if (updateDTO.getEmailPessoaContato() != null) negocio.setEmailPessoaContato(updateDTO.getEmailPessoaContato());
        if (updateDTO.getTelefonePessoaContato() != null) negocio.setTelefonePessoaContato(updateDTO.getTelefonePessoaContato());

        // Lógica para atualizar etapa (já estava correta)
        if (updateDTO.getEtapaId() != null) {
            Etapa novaEtapa = etapaRepository.findById(updateDTO.getEtapaId())
                    .orElseThrow(() -> new RuntimeException("Etapa não encontrada."));

            if (!novaEtapa.getFunil().getId().equals(negocio.getFunil().getId())) {
                throw new IllegalArgumentException("Não é possível mover o negócio para uma etapa de outro funil.");
            }
            negocio.setEtapa(novaEtapa);
        }

        Negocio negocioAtualizado = negocioRepository.save(negocio);
        return new NegocioResponseDTO(negocioAtualizado);
    }

    // --- CORREÇÃO no Repository: Remover método duplicado ---
    // Mantenha apenas o método correto no NegocioRepository.java
    @Transactional(readOnly = true)
    public List<NegocioResponseDTO> listarNegociosDoUsuario(String username) {
        // Usar findByFunilUserEmail (ou o nome correto definido no seu repositório)
        return negocioRepository.findByFunilUserEmail(username).stream()
                .map(NegocioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NegocioResponseDTO buscarNegocioPorId(Long id, String username) {
        Negocio negocio = negocioRepository.findById(id)
                // Adiciona a validação de segurança diretamente na busca
                .filter(n -> n.getFunil().getUser().getEmail().equals(username))
                .orElseThrow(() -> new SecurityException("Negócio não encontrado ou acesso negado."));
        return new NegocioResponseDTO(negocio);
    }

    @Transactional
    public void deletarNegocio(Long id, String username) {
        Negocio negocio = negocioRepository.findById(id)
                // Adiciona a validação de segurança diretamente na busca
                .filter(n -> n.getFunil().getUser().getEmail().equals(username))
                .orElseThrow(() -> new SecurityException("Negócio não encontrado ou acesso negado."));
        negocioRepository.delete(negocio);
    }
}