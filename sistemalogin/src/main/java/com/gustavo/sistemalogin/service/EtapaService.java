package com.gustavo.sistemalogin.service;

import com.gustavo.sistemalogin.dto.EtapaCreateDTO;
import com.gustavo.sistemalogin.dto.EtapaResponseDTO;
import com.gustavo.sistemalogin.dto.EtapaUpdateDTO;
import com.gustavo.sistemalogin.model.Etapa;
import com.gustavo.sistemalogin.model.Funil;
import com.gustavo.sistemalogin.repository.EtapaRepository;
import com.gustavo.sistemalogin.repository.FunilRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EtapaService {

    @Autowired
    private EtapaRepository etapaRepository;

    @Autowired
    private FunilRepository funilRepository;

    // Injeção de dependências via construtor (melhor prática)
    public EtapaService(EtapaRepository etapaRepository, FunilRepository funilRepository) {
        this.etapaRepository = etapaRepository;
        this.funilRepository = funilRepository;
    }

    @Transactional
    public EtapaResponseDTO criarEtapa(EtapaCreateDTO dto, String userEmail) {
        Funil funil = funilRepository.findById(dto.getFunilId(dto.getFunil()))
                .orElseThrow(() -> new RuntimeException("Funil não encontrado"));

        if (!funil.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Acesso negado: funil não pertence ao usuário.");
        }

        Etapa etapa = new Etapa();
        etapa.setNome(dto.getNome());
        etapa.setFunil(funil);

        Etapa savedEtapa = etapaRepository.save(etapa);
        return new EtapaResponseDTO(savedEtapa);
    }

    // Outros métodos (listar, atualizar, deletar) podem ser adicionados aqui
}
