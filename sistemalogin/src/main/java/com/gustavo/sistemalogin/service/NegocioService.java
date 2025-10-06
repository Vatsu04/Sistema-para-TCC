package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.NegocioCreateDTO;
import com.gustavo.sistemalogin.dto.NegocioResponseDTO;
import com.gustavo.sistemalogin.dto.NegocioUpdateDTO;
import com.gustavo.sistemalogin.model.Funil;
import com.gustavo.sistemalogin.model.Negocio;
import com.gustavo.sistemalogin.model.Pessoa;
import com.gustavo.sistemalogin.repository.FunilRepository;
import com.gustavo.sistemalogin.repository.NegocioRepository;
import com.gustavo.sistemalogin.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NegocioService {

    @Autowired
    private NegocioRepository negocioRepository;

    @Autowired
    private FunilRepository funilRepository;

    @Autowired
    private PessoaRepository pessoaRepository;


    public NegocioService(NegocioRepository negocioRepository, FunilRepository funilRepository, PessoaRepository pessoaRepository) {
        this.negocioRepository = negocioRepository;
        this.funilRepository = funilRepository;
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public NegocioResponseDTO criarNegocio(NegocioCreateDTO negocioDTO, String userEmail) {
        // 1. Busca e valida o Funil
        Funil funil = funilRepository.findById(negocioDTO.getFunil().getId())
                .orElseThrow(() -> new RuntimeException("Funil com ID " + negocioDTO.getFunil().getId() + " não encontrado."));
        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: o funil informado não pertence a você.");
        }

        // 2. Busca e valida a Pessoa
        Pessoa pessoa = pessoaRepository.findById(negocioDTO.getPessoa().getId())
                .orElseThrow(() -> new RuntimeException("Pessoa com ID " + negocioDTO.getPessoa().getId()+ " não encontrada."));
        if (!pessoa.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: o contato informado não pertence a você.");
        }

        // 3. Cria e salva o Negócio
        Negocio novoNegocio = new Negocio();
        novoNegocio.setTitulo(negocioDTO.getTitulo());
        novoNegocio.setEtapa(negocioDTO.getEtapa());
        novoNegocio.setPipeline_stage(negocioDTO.getPipeline_stage());
        novoNegocio.setId_ornizacao(negocioDTO.getId_organizacao());
        novoNegocio.setFunil(funil);
        novoNegocio.setPessoa(pessoa);

        Negocio negocioSalvo = negocioRepository.save(novoNegocio);
        return new NegocioResponseDTO(negocioSalvo);
    }

    @Transactional(readOnly = true)
    public NegocioResponseDTO findNegocioById(Long id, String userEmail) {
        Negocio negocio = negocioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Negócio com ID " + id + " não encontrado."));

        if (!negocio.getFunil().getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado.");
        }

        return new NegocioResponseDTO(negocio);
    }

    @Transactional(readOnly = true)
    public List<NegocioResponseDTO> findNegociosByFunil(Long funilId, String userEmail) {
        Funil funil = funilRepository.findById(funilId)
                .orElseThrow(() -> new RuntimeException("Funil com ID " + funilId + " não encontrado."));

        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado.");
        }

        return negocioRepository.findByFunilId(funilId).stream()
                .map(NegocioResponseDTO::new)
                .collect(Collectors.toList());
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

        // Atualiza os campos se eles não forem nulos no DTO
        if (updateDTO.getTitulo() != null) {
            negocio.setTitulo(updateDTO.getTitulo());
        }
        if (updateDTO.getEtapa() != null) {
            negocio.setEtapa(updateDTO.getEtapa());
        }
        if (updateDTO.getValor() != null) {
            negocio.setValor(updateDTO.getValor());
        }

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

    // Você pode adicionar os métodos de atualizar e deletar aqui, seguindo a mesma lógica de validação.
}
