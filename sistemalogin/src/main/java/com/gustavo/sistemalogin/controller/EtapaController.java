package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.EtapaCreateDTO;
import com.gustavo.sistemalogin.dto.EtapaResponseDTO;
import com.gustavo.sistemalogin.dto.EtapaUpdateDTO;
import com.gustavo.sistemalogin.service.EtapaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/etapas")
public class EtapaController {

    private final EtapaService etapaService;

    // Injeção de dependência via construtor (melhor prática)
    public EtapaController(EtapaService etapaService) {
        this.etapaService = etapaService;
    }

    /**
     * Endpoint para criar uma nova etapa dentro de um funil.
     */
    @PostMapping
    public ResponseEntity<EtapaResponseDTO> criarEtapa(
            @Valid @RequestBody EtapaCreateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        EtapaResponseDTO novaEtapa = etapaService.criarEtapa(dto, userDetails.getUsername());
        return new ResponseEntity<>(novaEtapa, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todas as etapas de um funil específico.
     * O ID do funil deve ser passado como um parâmetro na URL.
     * Exemplo de uso: GET /api/etapas?funilId=1
     */
    @GetMapping
    public ResponseEntity<List<EtapaResponseDTO>> listarEtapasDoFunil(
            @RequestParam Long funilId,
            @AuthenticationPrincipal UserDetails userDetails) {

        List<EtapaResponseDTO> etapas = etapaService.listarEtapasPorFunil(funilId, userDetails.getUsername());
        return ResponseEntity.ok(etapas);
    }

    /**
     * Endpoint para buscar uma etapa específica pelo seu ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EtapaResponseDTO> buscarEtapaPorId(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        EtapaResponseDTO etapa = etapaService.buscarEtapaPorId(id, userDetails.getUsername());
        return ResponseEntity.ok(etapa);
    }

    /**
     * Endpoint para atualizar uma etapa existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EtapaResponseDTO> atualizarEtapa(
            @PathVariable Long id,
            @Valid @RequestBody EtapaUpdateDTO dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        EtapaResponseDTO etapaAtualizada = etapaService.atualizarEtapa(id, dto, userDetails.getUsername());
        return ResponseEntity.ok(etapaAtualizada);
    }

    /**
     * Endpoint para deletar uma etapa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEtapa(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        etapaService.deletarEtapa(id, userDetails.getUsername());
        return ResponseEntity.noContent().build(); // Retorna 204 No Content, o padrão para delete
    }
}