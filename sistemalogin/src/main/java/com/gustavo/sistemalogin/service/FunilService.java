package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.FunilCreateDTO;
import com.gustavo.sistemalogin.dto.FunilResponseDTO;
import com.gustavo.sistemalogin.dto.FunilUpdateDTO;
import com.gustavo.sistemalogin.model.Funil;
import com.gustavo.sistemalogin.model.User;
import com.gustavo.sistemalogin.repository.FunilRepository;
import com.gustavo.sistemalogin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FunilService {

    @Autowired
    private FunilRepository funilRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public FunilResponseDTO criarFunil(FunilCreateDTO funilDTO, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));
        Funil novoFunil = new Funil();
        novoFunil.setNome(funilDTO.getNome());
        novoFunil.setUser(user);

        Funil funilSalvo = funilRepository.save(novoFunil);
        return new FunilResponseDTO(funilSalvo);
    }

    @Transactional(readOnly = true)
    public FunilResponseDTO findFunilById(Long id, String userEmail) {
        Funil funil = funilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funil com ID " + id + " não encontrado."));

        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: este funil não pertence ao seu usuário.");
        }
        return new FunilResponseDTO(funil);
    }

    @Transactional(readOnly = true)
    public List<FunilResponseDTO> findFunisByUser(String userEmail) {
        return funilRepository.findByUserEmail(userEmail).stream()
                .map(FunilResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public FunilResponseDTO atualizarFunil(Long id, FunilUpdateDTO funilDTO, String userEmail) {
        Funil funil = funilRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Funil com ID " + id + " não encontrado."));

        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado.");
        }

        funil.setNome(funilDTO.getNome());
        Funil funilAtualizado = funilRepository.save(funil);
        return new FunilResponseDTO(funil);
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

        // Validação de segurança: garante que o utilizador só pode aceder aos seus próprios funis.
        if (!funil.getUser().getEmail().equals(username)) {
            throw new SecurityException("Acesso negado: Este funil não pertence ao utilizador.");
        }

        return new FunilResponseDTO(funil);
    }
}
