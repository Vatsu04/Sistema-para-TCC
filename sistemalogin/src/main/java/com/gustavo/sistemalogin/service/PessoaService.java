package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.PessoaCreateDTO;
import com.gustavo.sistemalogin.dto.PessoaResponseDTO;
import com.gustavo.sistemalogin.dto.PessoaUpdateDTO;
import com.gustavo.sistemalogin.model.Pessoa;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.PessoaRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private UserRepository userRepository;


    public PessoaService(PessoaRepository pessoaRepository, UserRepository userRepository) {
        this.pessoaRepository = pessoaRepository;
        this.userRepository = userRepository;
    }
    /**
     * Cria uma nova Pessoa (contato) associada ao usuário autenticado.
     * @param pessoaDTO DTO com os dados da nova pessoa.
     * @param userEmail Email do usuário autenticado.
     * @return DTO com os dados da pessoa criada.
     */
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
        novaPessoa.setUser(user); // Associa a pessoa ao usuário logado

        Pessoa pessoaSalva = pessoaRepository.save(novaPessoa);
        return new PessoaResponseDTO(pessoaSalva);
    }

    /**
     * Busca uma pessoa pelo ID, garantindo que ela pertença ao usuário autenticado.
     * @param id ID da pessoa.
     * @param userEmail Email do usuário autenticado.
     * @return DTO da pessoa encontrada.
     */
    @Transactional(readOnly = true)
    public PessoaResponseDTO findPessoaById(Long id, String userEmail) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa com ID " + id + " não encontrada."));

        if (!pessoa.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: esta pessoa não pertence ao seu usuário.");
        }

        return new PessoaResponseDTO(pessoa);
    }

    /**
     * Lista todas as pessoas que pertencem ao usuário autenticado.
     * @param userEmail Email do usuário autenticado.
     * @return Lista de DTOs das pessoas encontradas.
     */
    @Transactional(readOnly = true)
    public List<PessoaResponseDTO> findPessoasByUser(String userEmail) {
        return pessoaRepository.findByUserEmail(userEmail).stream()
                .map(PessoaResponseDTO::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza os dados de uma pessoa existente.
     * @param id ID da pessoa a ser atualizada.
     * @param pessoaUpdateDTO DTO com os novos dados.
     * @param userEmail Email do usuário autenticado.
     * @return DTO da pessoa atualizada.
     */
    @Transactional
    public PessoaResponseDTO atualizarPessoa(Long id, PessoaUpdateDTO pessoaUpdateDTO, String userEmail) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa com ID " + id + " não encontrada."));

        if (!pessoa.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: você não pode atualizar esta pessoa.");
        }

        // Atualiza apenas os campos que foram fornecidos no DTO
        if (pessoaUpdateDTO.getNome() != null) pessoa.setNome(pessoaUpdateDTO.getNome());
        if (pessoaUpdateDTO.getEmail() != null) pessoa.setEmail(pessoaUpdateDTO.getEmail());
        if (pessoaUpdateDTO.getTelefone() != null) pessoa.setTelefone(pessoaUpdateDTO.getTelefone());
        if (pessoaUpdateDTO.getCpf() != null) pessoa.setCpf(pessoaUpdateDTO.getCpf());
        if (pessoaUpdateDTO.getRg() != null) pessoa.setRg(pessoaUpdateDTO.getRg());
        if (pessoaUpdateDTO.getData_de_nascimento() != null) pessoa.setData_de_nascimento(pessoaUpdateDTO.getData_de_nascimento());

        Pessoa pessoaAtualizada = pessoaRepository.save(pessoa);
        return new PessoaResponseDTO(pessoaAtualizada);
    }

    /**
     * Deleta uma pessoa pelo ID.
     * @param id ID da pessoa a ser deletada.
     * @param userEmail Email do usuário autenticado.
     */
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
    public List<PessoaResponseDTO> listarPessoasDoUsuario(String username) {
        return pessoaRepository.findByUserEmail(username).stream()
                .map(PessoaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PessoaResponseDTO buscarPessoaPorId(Long id, String username) {
        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com o ID: " + id));

        // Validação de segurança: garante que o utilizador só pode aceder aos seus próprios contatos
        if (!pessoa.getUser().getEmail().equals(username)) {
            throw new SecurityException("Acesso negado: Este contato não pertence ao utilizador.");
        }

        return new PessoaResponseDTO(pessoa);
    }
}

