package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.PessoaCreateDTO;
import com.gustavo.sistemalogin.dto.PessoaResponseDTO;
import com.gustavo.sistemalogin.dto.PessoaUpdateDTO;
import com.gustavo.sistemalogin.service.PessoaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerir as operações CRUD de Pessoas (Contatos).
 * Todos os endpoints aqui são protegidos e requerem um token JWT válido.
 */
@RestController
@RequestMapping("/api/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    // Injeção de dependência via construtor (melhor prática)
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    /**
     * Endpoint para criar uma nova Pessoa (contato).
     * @param pessoaCreateDTO DTO com os dados para a criação da pessoa.
     * @param userDetails Detalhes do utilizador autenticado, injetados automaticamente pelo Spring Security.
     * @return A pessoa recém-criada.
     */
    @PostMapping
    public ResponseEntity<PessoaResponseDTO> criarPessoa(@RequestBody PessoaCreateDTO pessoaCreateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        // O email do utilizador logado é extraído do UserDetails e passado para o serviço
        PessoaResponseDTO novaPessoa = pessoaService.criarPessoa(pessoaCreateDTO, userDetails.getUsername());
        return new ResponseEntity<>(novaPessoa, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todas as pessoas do utilizador autenticado.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return Uma lista com as pessoas do utilizador.
     */
    @GetMapping
    public ResponseEntity<List<PessoaResponseDTO>> listarPessoasDoUsuario(@AuthenticationPrincipal UserDetails userDetails) {
        List<PessoaResponseDTO> pessoas = pessoaService.listarPessoasDoUsuario(userDetails.getUsername());
        return ResponseEntity.ok(pessoas);
    }

    /**
     * Endpoint para buscar uma pessoa específica pelo seu ID.
     * @param id O ID da pessoa a ser buscada.
     * @param userDetails Detalhes do utilizador autenticado (para verificação de permissão).
     * @return A pessoa encontrada.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> buscarPessoaPorId(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        PessoaResponseDTO pessoa = pessoaService.buscarPessoaPorId(id, userDetails.getUsername());
        return ResponseEntity.ok(pessoa);
    }

    /**
     * Endpoint para atualizar uma pessoa existente.
     * @param id O ID da pessoa a ser atualizada.
     * @param pessoaUpdateDTO DTO com os dados a serem atualizados.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return A pessoa atualizada.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaResponseDTO> atualizarPessoa(@PathVariable Long id,  @RequestBody PessoaUpdateDTO pessoaUpdateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        PessoaResponseDTO pessoaAtualizada = pessoaService.atualizarPessoa(id, pessoaUpdateDTO, userDetails.getUsername());
        return ResponseEntity.ok(pessoaAtualizada);
    }

    /**
     * Endpoint para deletar uma pessoa.
     * @param id O ID da pessoa a ser deletada.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return Uma resposta vazia com status 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        pessoaService.deletarPessoa(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}

