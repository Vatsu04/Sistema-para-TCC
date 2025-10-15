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
    private final PessoaRepository pessoaRepository;
    private final OrganizacaoRepository organizacaoRepository;
    private final EtapaRepository etapaRepository; // Repositório necessário

    public NegocioService(NegocioRepository negocioRepository, FunilRepository funilRepository,
                          PessoaRepository pessoaRepository, OrganizacaoRepository organizacaoRepository,
                          EtapaRepository etapaRepository) {
        this.negocioRepository = negocioRepository;
        this.funilRepository = funilRepository;
        this.pessoaRepository = pessoaRepository;
        this.organizacaoRepository = organizacaoRepository;
        this.etapaRepository = etapaRepository;
    }

    @Transactional
    public NegocioResponseDTO criarNegocio(NegocioCreateDTO dto, String userEmail) {
        // Validações de propriedade (o usuário é dono dos recursos?)
        Funil funil = funilRepository.findById(dto.getFunilId())
                .filter(f -> f.getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new SecurityException("Funil não encontrado ou não pertence ao usuário."));

        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .filter(p -> p.getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new SecurityException("Pessoa não encontrada ou não pertence ao usuário."));

        Organizacao organizacao = organizacaoRepository.findById(dto.getOrganizacaoId())
                .filter(o -> o.getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new SecurityException("Organização não encontrada ou não pertence ao usuário."));

        Etapa etapa = etapaRepository.findById(dto.getEtapaId())
                .orElseThrow(() -> new RuntimeException("Etapa não encontrada."));

        // Validação de consistência (a etapa pertence ao funil?)
        if (!etapa.getFunil().getId().equals(funil.getId())) {
            throw new IllegalArgumentException("A etapa informada não pertence ao funil especificado.");
        }

        Negocio novoNegocio = new Negocio();
        novoNegocio.setTitulo(dto.getTitulo());
        novoNegocio.setValor(dto.getValor());
        novoNegocio.setDataDeAbertura(LocalDate.now());
        novoNegocio.setDataDeFechamento(dto.getData_de_fechamento());
        novoNegocio.setEtapa(etapa);
        novoNegocio.setFunil(funil);
        novoNegocio.setPessoa(pessoa);
        novoNegocio.setOrganizacao(organizacao);

        Negocio negocioSalvo = negocioRepository.save(novoNegocio);
        return new NegocioResponseDTO(negocioSalvo);
    }
    @Transactional(readOnly = true)
    public List<NegocioResponseDTO> listarNegociosDoUsuario(String username) {
        return negocioRepository.findByFunilUserEmail(username).stream()
                .map(NegocioResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NegocioResponseDTO buscarNegocioPorId(Long id, String username) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));
        if (!negocio.getFunil().getUser().getEmail().equals(username)) {
            throw new SecurityException("Acesso negado.");
        }
        return new NegocioResponseDTO(negocio);
    }

    @Transactional
    public NegocioResponseDTO atualizarNegocio(Long id, NegocioUpdateDTO updateDTO, String username) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));
        if (!negocio.getFunil().getUser().getEmail().equals(username)) {
            throw new SecurityException("Acesso negado.");
        }

        if (updateDTO.getTitulo() != null) negocio.setTitulo(updateDTO.getTitulo());
        if (updateDTO.getValor() != null) negocio.setValor(updateDTO.getValor());
        if (updateDTO.getData_de_fechamento() != null) negocio.setDataDeFechamento(updateDTO.getData_de_fechamento());

        // --- LÓGICA DE ATUALIZAÇÃO DA ETAPA CORRIGIDA ---
        if (updateDTO.getEtapaId() != null) {
            Etapa novaEtapa = etapaRepository.findById(updateDTO.getEtapaId())
                    .orElseThrow(() -> new RuntimeException("Etapa com ID " + updateDTO.getEtapaId() + " não encontrada."));

            // Validação de consistência: a nova etapa deve pertencer ao mesmo funil do negócio.
            if (!novaEtapa.getFunil().getId().equals(negocio.getFunil().getId())) {
                throw new IllegalArgumentException("Não é possível mover o negócio para uma etapa de outro funil.");
            }
            negocio.setEtapa(novaEtapa);
        }

        // Adicionar lógica para atualizar pessoa, funil e organização, se necessário...

        Negocio negocioAtualizado = negocioRepository.save(negocio);
        return new NegocioResponseDTO(negocioAtualizado);
    }

    @Transactional
    public void deletarNegocio(Long id, String username) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negócio não encontrado"));
        if (!negocio.getFunil().getUser().getEmail().equals(username)) {
            throw new SecurityException("Acesso negado.");
        }
        negocioRepository.delete(negocio);
    }
}