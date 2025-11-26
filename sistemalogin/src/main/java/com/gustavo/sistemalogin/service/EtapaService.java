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
import com.gustavo.sistemalogin.repository.NegocioRepository;

@Service
public class EtapaService {

    private final EtapaRepository etapaRepository;
    private final FunilRepository funilRepository;
    private final NegocioRepository negocioRepository;

    public EtapaService(EtapaRepository etapaRepository, FunilRepository funilRepository, NegocioRepository negocioRepository) {
        this.etapaRepository = etapaRepository;
        this.funilRepository = funilRepository;
        this.negocioRepository = negocioRepository;
    }

    @Transactional
    public EtapaResponseDTO criarEtapa(EtapaCreateDTO dto, String userEmail) {

        Funil funil = funilRepository.findById(dto.getFunilId())
                .filter(f -> f.getUser().getEmail().equals(userEmail))
                .orElseThrow(() -> new SecurityException("Funil não encontrado ou não pertence ao usuário."));

        // --- INÍCIO DA LÓGICA DE POSIÇÃO AUTOMÁTICA ---
        // 1. Busca as etapas atuais do funil usando o método do repositório
        List<Etapa> etapasAtuais = etapaRepository.findByFunilId(funil.getId());

        // 2. A nova posição é o número de etapas existentes.
        //    - Se 0 etapas existem, nova posição é 0.
        //    - Se 1 etapa existe (pos 0), nova posição é 1.
        //    - Se 2 etapas existem (pos 0, 1), nova posição é 2.
        int novaPosicao = etapasAtuais.size();
        // --- FIM DA LÓGICA DE POSIÇÃO AUTOMÁTICA ---

        Etapa novaEtapa = new Etapa();
        novaEtapa.setNome(dto.getNome());
        novaEtapa.setPosicao(novaPosicao); // <-- Seta a posição calculada
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

        // Atualiza o nome se for fornecido
        if (dto.getNome() != null && !dto.getNome().isBlank()) {
            etapa.setNome(dto.getNome());
        }

        // Nota: A lógica para ATUALIZAR a posição (drag-and-drop)
        // exigiria reordenar as outras etapas e não está implementada aqui.
        if (dto.getPosicao() != null) {
            // TODO: Implementar lógica de reordenação se necessário
            etapa.setPosicao(dto.getPosicao());
        }

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

        negocioRepository.deleteByEtapaId(etapaId);
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
