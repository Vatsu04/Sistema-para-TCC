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
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NegocioService {

    private final NegocioRepository negocioRepository;
    private final FunilRepository funilRepository;
    private final PessoaRepository pessoaRepository;
    private final OrganizacaoRepository organizacaoRepository;

    public NegocioService(NegocioRepository negocioRepository, FunilRepository funilRepository,
                          PessoaRepository pessoaRepository, OrganizacaoRepository organizacaoRepository) {
        this.negocioRepository = negocioRepository;
        this.funilRepository = funilRepository;
        this.pessoaRepository = pessoaRepository;
        this.organizacaoRepository = organizacaoRepository;
    }

    @Transactional
    public NegocioResponseDTO criarNegocio(NegocioCreateDTO dto, String userEmail) {
        Funil funil = funilRepository.findById(dto.getFunilId())
                .orElseThrow(() -> new RuntimeException("Funil não encontrado."));
        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: Funil não pertence a este usuário.");
        }

        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada."));
        if (!pessoa.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: Contato não pertence a este usuário.");
        }

        Organizacao organizacao = organizacaoRepository.findById(dto.getOrganizacaoId())
                .orElseThrow(() -> new RuntimeException("Organização não encontrada."));
        if (!organizacao.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: Organização não pertence a este usuário.");
        }

        Negocio novoNegocio = new Negocio();
        novoNegocio.setTitulo(dto.getTitulo());
        novoNegocio.setValor(dto.getValor());
        novoNegocio.setEtapa(dto.getEtapa());
        novoNegocio.setDataDeAbertura(LocalDate.now());
        novoNegocio.setData_de_fechamento(dto.getData_de_fechamento());
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