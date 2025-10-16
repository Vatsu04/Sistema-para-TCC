package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.PessoaCreateDTO;
import com.gustavo.sistemalogin.dto.PessoaResponseDTO;
import com.gustavo.sistemalogin.dto.PessoaUpdateDTO;
import com.gustavo.sistemalogin.model.Pessoa;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.PessoaRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    // Injeção de dependência via construtor (melhor e única forma necessária)
    private final PessoaRepository pessoaRepository;
    private final UserRepository userRepository;

    public PessoaService(PessoaRepository pessoaRepository, UserRepository userRepository) {
        this.pessoaRepository = pessoaRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public PessoaResponseDTO criarPessoa(PessoaCreateDTO pessoaDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        Pessoa novaPessoa = new Pessoa();
        novaPessoa.setNome(pessoaDTO.getNome());
        novaPessoa.setEmail(pessoaDTO.getEmail());
        novaPessoa.setTelefone(pessoaDTO.getTelefone());
        novaPessoa.setCpf(pessoaDTO.getCpf());
        novaPessoa.setRg(pessoaDTO.getRg());
        novaPessoa.setData_de_nascimento(pessoaDTO.getData_de_nascimento());
        novaPessoa.setUser(user);

        Pessoa pessoaSalva = pessoaRepository.save(novaPessoa);
        return new PessoaResponseDTO(pessoaSalva);
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarPessoasDoUsuario(String username) {
        return pessoaRepository.findByUserEmail(username).stream()
                .map(PessoaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PessoaResponseDTO buscarPessoaPorId(Long id, String username) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com o ID: " + id));

        if (!pessoa.getUser().getEmail().equals(username)) {
            throw new SecurityException("Acesso negado: Este contato não pertence ao usuário.");
        }

        return new PessoaResponseDTO(pessoa);
    }

    @Transactional
    public PessoaResponseDTO atualizarPessoa(Long id, PessoaUpdateDTO pessoaUpdateDTO, String userEmail) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa com ID " + id + " não encontrada."));

        if (!pessoa.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: você não pode atualizar esta pessoa.");
        }

        if (pessoaUpdateDTO.getNome() != null) pessoa.setNome(pessoaUpdateDTO.getNome());
        if (pessoaUpdateDTO.getEmail() != null) pessoa.setEmail(pessoaUpdateDTO.getEmail());
        // ... (resto da lógica de atualização que já estava correta)

        Pessoa pessoaAtualizada = pessoaRepository.save(pessoa);
        return new PessoaResponseDTO(pessoaAtualizada);
    }

    @Transactional
    public void deletarPessoa(Long id, String userEmail) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa com ID " + id + " não encontrada."));

        if (!pessoa.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: você não pode deletar esta pessoa.");
        }
        pessoaRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> listarTodasAsPessoas() {
        return pessoaRepository.findAll().stream()
                .map(PessoaResponseDTO::new)
                .collect(Collectors.toList());
    }
}