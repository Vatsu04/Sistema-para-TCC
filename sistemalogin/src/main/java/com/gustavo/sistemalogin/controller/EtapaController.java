package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.EtapaCreateDTO;
import com.gustavo.sistemalogin.dto.EtapaResponseDTO;
import com.gustavo.sistemalogin.service.EtapaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/etapas")
public class EtapaController {

    @Autowired
    private EtapaService etapaService;

    @PostMapping
    public ResponseEntity<EtapaResponseDTO> criarEtapa(@RequestBody EtapaCreateDTO dto, Authentication authentication) {
        String userEmail = authentication.getName();
        EtapaResponseDTO response = etapaService.criarEtapa(dto, userEmail);
        return ResponseEntity.ok(response);
    }
}
