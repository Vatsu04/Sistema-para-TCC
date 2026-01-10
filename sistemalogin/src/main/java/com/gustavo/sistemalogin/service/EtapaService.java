package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.EtapaCreateDTO;
import com.gustavo.sistemalogin.dto.EtapaResponseDTO;
import com.gustavo.sistemalogin.dto.EtapaUpdateDTO;
import com.gustavo.sistemalogin.model.Etapa;
import com.gustavo.sistemalogin.model.Funil;
import com.gustavo.sistemalogin.repository.EtapaRepository;
import com.gustavo.sistemalogin.repository.FunilRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EtapaService {

    private final EtapaRepository etapaRepository;
    private final FunilRepository funilRepository;

    public EtapaService(EtapaRepository etapaRepository, FunilRepository funilRepository) {
        this.etapaRepository = etapaRepository;
        this.funilRepository = funilRepository;
    }

    @Transactional
    public EtapaResponseDTO criarEtapa(EtapaCreateDTO dto, String userEmail) {

        Funil funil = funilRepository.findById(dto.getFunilId())
                .filter(f -> f.getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new SecurityException("Funil não encontrado ou não pertence ao usuário."));

        Etapa novaEtapa = new Etapa();
        novaEtapa.setNome(dto.getNome());
        novaEtapa.setPosicao(dto.getPosicao());
        novaEtapa.setFunil(funil);

        Etapa etapaSalva = etapaRepository.save(novaEtapa);
        return new EtapaResponseDTO(etapaSalva);
    }

    @Transactional(readOnly = true)
    public List<EtapaResponseDTO> listarEtapasPorFunil(Long funilId, String userEmail) {
        // Valida se o utilizador é o dono do funil antes de listar as etapas
        findFunilByIdAndValidateOwner(funilId, userEmail);

        return etapaRepository.findByFunilId(funilId).stream()
                .map(EtapaResponseDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public EtapaResponseDTO atualizarEtapa(Long etapaId, EtapaUpdateDTO dto, String userEmail) {
        Etapa etapa = findEtapaByIdAndValidateOwner(etapaId, userEmail);

        etapa.setNome(dto.getNome());

        Etapa etapaAtualizada = etapaRepository.save(etapa);
        return new EtapaResponseDTO(etapaAtualizada);
    }

    /**
     * Deleta uma etapa.
     */
    @Transactional
    public void deletarEtapa(Long etapaId, String userEmail) {
        // Valida a propriedade antes de deletar
        findEtapaByIdAndValidateOwner(etapaId, userEmail);
        etapaRepository.deleteById(etapaId);
    }

    // --- Métodos Auxiliares Privados ---

    private Funil findFunilByIdAndValidateOwner(Long funilId, String userEmail) {
        Funil funil = funilRepository.findById(funilId)
                .orElseThrow(() -> new RuntimeException("Funil com ID " + funilId + " não encontrado."));

        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: este funil não pertence ao seu utilizador.");
        }
        return funil;
    }


    private Etapa findEtapaByIdAndValidateOwner(Long etapaId, String userEmail) {
        Etapa etapa = etapaRepository.findById(etapaId)
                .orElseThrow(() -> new RuntimeException("Etapa com ID " + etapaId + " não encontrada."));

        if (!etapa.getFunil().getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: esta etapa não pertence a um funil do seu utilizador.");
        }
        return etapa;
    }

    @Transactional(readOnly = true)
    public EtapaResponseDTO buscarEtapaPorId(Long etapaId, String userEmail) {
        Etapa etapa = findEtapaByIdAndValidateOwner(etapaId, userEmail);
        return new EtapaResponseDTO(etapa);
    }


}
