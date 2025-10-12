package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.OrganizacaoCreateDTO;
import com.gustavo.sistemalogin.dto.OrganizacaoResponseDTO;
import com.gustavo.sistemalogin.dto.OrganizacaoUpdateDTO;
import com.gustavo.sistemalogin.service.OrganizacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organizacoes")
public class OrganizacaoController {

    @Autowired
    private OrganizacaoService organizacaoService;

    @PostMapping
    public ResponseEntity<OrganizacaoResponseDTO> criarOrganizacao(@RequestBody OrganizacaoCreateDTO dto, Authentication authentication) {
        String userEmail = authentication.getName();
        OrganizacaoResponseDTO response = organizacaoService.criarOrganizacao(dto, userEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrganizacaoResponseDTO>> listarOrganizacoes(Authentication authentication) {
        String userEmail = authentication.getName();
        List<OrganizacaoResponseDTO> response = organizacaoService.findOrganizacoesByUser(userEmail);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizacaoResponseDTO> atualizarOrganizacao(@PathVariable Long id, @RequestBody OrganizacaoUpdateDTO dto, Authentication authentication) {
        String userEmail = authentication.getName();
        OrganizacaoResponseDTO response = organizacaoService.atualizarOrganizacao(id, dto, userEmail);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarOrganizacao(@PathVariable Long id, Authentication authentication) {
        String userEmail = authentication.getName();
        organizacaoService.deletarOrganizacao(id, userEmail);
        return ResponseEntity.noContent().build();
    }
}
