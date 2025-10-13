package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.FunilCreateDTO;
import com.gustavo.sistemalogin.dto.FunilResponseDTO;
import com.gustavo.sistemalogin.dto.FunilUpdateDTO;
import com.gustavo.sistemalogin.model.Funil;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.FunilRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunilService {

    private final FunilRepository funilRepository;
    private final UserRepository userRepository;

    public FunilService(FunilRepository funilRepository, UserRepository userRepository) {
        this.funilRepository = funilRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public FunilResponseDTO criarFunil(FunilCreateDTO funilDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Utilizador não encontrado."));
        Funil novoFunil = new Funil();

        // --- LÓGICA CORRIGIDA ---
        novoFunil.setNome(funilDTO.getNome());
        novoFunil.setEmail(funilDTO.getEmail()); // Adicionado
        novoFunil.setTelefone(funilDTO.getTelefone()); // Adicionado
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


        funil.setNome(funilDTO.getNome());
        funil.setEmail(funilDTO.getEmail());
        funil.setTelefone(funilDTO.getTelefone());

        Funil funilAtualizado = funilRepository.save(funil);
        return new FunilResponseDTO(funilAtualizado);
    }

    @Transactional
    public void deletarFunil(Long id, String userEmail) {
        Funil funil = funilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funil com ID " + id + " não encontrado."));

        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado.");
        }
        funilRepository.deleteById(id);
    }
}

