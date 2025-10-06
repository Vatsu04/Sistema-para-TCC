package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.PessoaCreateDTO;
import com.gustavo.sistemalogin.dto.PessoaResponseDTO;
import com.gustavo.sistemalogin.dto.PessoaUpdateDTO;
import com.gustavo.sistemalogin.service.PessoaService;
import jakarta.validation.Valid; // <-- Garanta que este import está correto
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @PostMapping
    public ResponseEntity<PessoaResponseDTO> criarPessoa(@Valid @RequestBody PessoaCreateDTO pessoaCreateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        PessoaResponseDTO novaPessoa = pessoaService.criarPessoa(pessoaCreateDTO, userDetails.getUsername());
        return new ResponseEntity<>(novaPessoa, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> listarPessoasDoUsuario(@AuthenticationPrincipal UserDetails userDetails) {
        List<PessoaResponseDTO> pessoas = pessoaService.listarPessoasDoUsuario(userDetails.getUsername());
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> buscarPessoaPorId(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        PessoaResponseDTO pessoa = pessoaService.buscarPessoaPorId(id, userDetails.getUsername());
        return ResponseEntity.ok(pessoa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> atualizarPessoa(@PathVariable Long id, @Valid @RequestBody PessoaUpdateDTO pessoaUpdateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        PessoaResponseDTO pessoaAtualizada = pessoaService.atualizarPessoa(id, pessoaUpdateDTO, userDetails.getUsername());
        return ResponseEntity.ok(pessoaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        pessoaService.deletarPessoa(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}

