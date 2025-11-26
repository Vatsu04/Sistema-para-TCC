package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.FunilCreateDTO;
import com.gustavo.sistemalogin.dto.FunilResponseDTO;
import com.gustavo.sistemalogin.dto.FunilUpdateDTO;
import com.gustavo.sistemalogin.model.Funil;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.EtapaRepository;
import com.gustavo.sistemalogin.repository.FunilRepository;
import com.gustavo.sistemalogin.repository.NegocioRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunilService {

    private final FunilRepository funilRepository;
    private final UserRepository userRepository;
    private final EtapaRepository etapaRepository;
    private final NegocioRepository negocioRepository;

    public FunilService(FunilRepository funilRepository,
                        UserRepository userRepository,
                        EtapaRepository etapaRepository,
                        NegocioRepository negocioRepository) {
        this.funilRepository = funilRepository;
        this.userRepository = userRepository;
        this.etapaRepository = etapaRepository;
        this.negocioRepository = negocioRepository;
    }

    @Transactional
    public FunilResponseDTO criarFunil(FunilCreateDTO funilDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado."));

        Funil novoFunil = new Funil();

        // Apenas definimos o nome e o usuário
        novoFunil.setNome(funilDTO.getNome());
        novoFunil.setUser(user);

        Funil funilSalvo = funilRepository.save(novoFunil);
        return new FunilResponseDTO(funilSalvo);
    }

    @Transactional(readOnly = true)
    public List<FunilResponseDTO> listarFunisDoUsuario(String username) {
        return funilRepository.findByUserEmail(username).stream()
                .map(FunilResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FunilResponseDTO buscarFunilPorId(Long id, String username) {
        Funil funil = funilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funil não encontrado com o ID: " + id));

        if (!funil.getUser().getEmail().equals(username)) {
            throw new SecurityException("Acesso negado: Este funil não pertence ao utilizador.");
        }

        return new FunilResponseDTO(funil);
    }

    @Transactional
    public FunilResponseDTO atualizarFunil(Long id, FunilUpdateDTO funilDTO, String userEmail) {
        Funil funil = funilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funil com ID " + id + " não encontrado."));

        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado.");
        }

        // Atualiza apenas o nome se ele for fornecido
        if (funilDTO.getNome() != null && !funilDTO.getNome().isBlank()) {
            funil.setNome(funilDTO.getNome());
        }

        Funil funilAtualizado = funilRepository.save(funil);
        return new FunilResponseDTO(funilAtualizado);
    }

    @Transactional // <--- Essencial para garantir que tudo seja apagado ou nada seja
    public void deletarFunil(Long id, String userEmail) {
        Funil funil = funilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funil com ID " + id + " não encontrado."));

        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado.");
        }

        // 1. Primeiro deletamos os NEGÓCIOS (Netos)
        negocioRepository.deleteByFunilId(id);

        // 2. Depois deletamos as ETAPAS (Filhos)
        etapaRepository.deleteByFunilId(id);

        // 3. Por fim, deletamos o FUNIL (Pai)
        funilRepository.deleteById(id);
    }
}