package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.NegocioCreateDTO;
import com.gustavo.sistemalogin.dto.NegocioResponseDTO;
import com.gustavo.sistemalogin.dto.NegocioUpdateDTO;
import com.gustavo.sistemalogin.model.Funil;
import com.gustavo.sistemalogin.model.Negocio;
import com.gustavo.sistemalogin.model.Organizacao;
import com.gustavo.sistemalogin.model.Pessoa;
import com.gustavo.sistemalogin.repository.FunilRepository;
import com.gustavo.sistemalogin.repository.NegocioRepository;
import com.gustavo.sistemalogin.repository.OrganizacaoRepository;
import com.gustavo.sistemalogin.repository.PessoaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NegocioService {

    // Injeção de dependência via construtor (melhor prática)
    private final NegocioRepository negocioRepository;
    private final FunilRepository funilRepository;
    private final PessoaRepository pessoaRepository;
    private final OrganizacaoRepository organizacaoRepository; // Repositório que estava faltando

    // O Spring irá injetar as instâncias automaticamente aqui
    public NegocioService(NegocioRepository negocioRepository, FunilRepository funilRepository,
                          PessoaRepository pessoaRepository, OrganizacaoRepository organizacaoRepository) {
        this.negocioRepository = negocioRepository;
        this.funilRepository = funilRepository;
        this.pessoaRepository = pessoaRepository;
        this.organizacaoRepository = organizacaoRepository;
    }

    @Transactional
    public NegocioResponseDTO criarNegocio(NegocioCreateDTO negocioDTO, String userEmail) {
        // 1. Busca e valida o Funil usando o ID do DTO
        Funil funil = funilRepository.findById(negocioDTO.getFunilId())
                .orElseThrow(() -> new RuntimeException("Funil com ID " + negocioDTO.getFunilId() + " não encontrado."));
        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: o funil informado não pertence a você.");
        }

        // 2. Busca e valida a Pessoa usando o ID do DTO
        Pessoa pessoa = pessoaRepository.findById(negocioDTO.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa com ID " + negocioDTO.getPessoaId() + " não encontrada."));
        if (!pessoa.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: o contato informado não pertence a você.");
        }

        // 3. Busca e valida a Organização usando o ID do DTO
        Organizacao organizacao = organizacaoRepository.findById(negocioDTO.getOrganizacaoId())
                .orElseThrow(() -> new RuntimeException("Organização com ID " + negocioDTO.getOrganizacaoId() + " não encontrada."));
        if (!organizacao.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: a organização informada não pertence a você.");
        }

        // 4. Se todas as validações passaram, cria e salva o Negócio
        Negocio novoNegocio = new Negocio();
        novoNegocio.setTitulo(negocioDTO.getTitulo());
        novoNegocio.setValor(negocioDTO.getValor());
        novoNegocio.setEtapa(negocioDTO.getEtapa());
        novoNegocio.setData_de_fechamento(negocioDTO.getData_de_fechamento());
        novoNegocio.setFunil(funil);
        novoNegocio.setPessoa(pessoa);
        novoNegocio.setOrganizacao(organizacao); // Assumindo que a entidade Negocio tem o relacionamento

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

        // Lógica de atualização parcial...
        if (updateDTO.getTitulo() != null) negocio.setTitulo(updateDTO.getTitulo());
        if (updateDTO.getEtapa() != null) negocio.setEtapa(updateDTO.getEtapa());
        if (updateDTO.getValor() != null) negocio.setValor(updateDTO.getValor());

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