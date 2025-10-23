package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.NegocioCreateDTO;
import com.gustavo.sistemalogin.dto.NegocioResponseDTO;
import com.gustavo.sistemalogin.dto.NegocioUpdateDTO;
import com.gustavo.sistemalogin.service.NegocioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import jakarta.validation.Valid;
/**
 * Controller para gerir as operações CRUD de Negócios.
 * Todos os endpoints são protegidos e requerem autenticação.
 */
@RestController
@RequestMapping("/api/negocios")
public class NegocioController {

    private final NegocioService negocioService;

    public NegocioController(NegocioService negocioService) {
        this.negocioService = negocioService;
    }

    /**
     * Endpoint para criar um novo negócio.
     * @param negocioCreateDTO DTO com os dados para a criação do negócio.
     * @param userDetails Detalhes do utilizador autenticado, injetados pelo Spring Security.
     * @return O negócio recém-criado.
     */
    @PostMapping
    public ResponseEntity<NegocioResponseDTO> criarNegocio(@Valid @RequestBody NegocioCreateDTO negocioCreateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        // Extrai o email (username) do utilizador autenticado e passa para o serviço
        NegocioResponseDTO novoNegocio = negocioService.criarNegocio(negocioCreateDTO, userDetails.getUsername());
        return new ResponseEntity<>(novoNegocio, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todos os negócios do utilizador autenticado.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return Uma lista de negócios.
     */
    @GetMapping
    public ResponseEntity<List<NegocioResponseDTO>> listarNegociosDoUsuario(@AuthenticationPrincipal UserDetails userDetails) {
        List<NegocioResponseDTO> negocios = negocioService.listarNegociosDoUsuario(userDetails.getUsername());
        return ResponseEntity.ok(negocios);
    }

    /**
     * Endpoint para buscar um negócio específico pelo ID.
     * @param id O ID do negócio a ser buscado.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return O negócio encontrado.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NegocioResponseDTO> buscarNegocioPorId(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        NegocioResponseDTO negocio = negocioService.buscarNegocioPorId(id, userDetails.getUsername());
        return ResponseEntity.ok(negocio);
    }

    /**
     * Endpoint para atualizar um negócio existente.
     * @param id O ID do negócio a ser atualizado.
     * @param negocioUpdateDTO DTO com os dados a serem atualizados.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return O negócio atualizado.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NegocioResponseDTO> atualizarNegocio(@PathVariable Long id,@Valid @RequestBody NegocioUpdateDTO negocioUpdateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        NegocioResponseDTO negocioAtualizado = negocioService.atualizarNegocio(id, negocioUpdateDTO, userDetails.getUsername());
        return ResponseEntity.ok(negocioAtualizado);
    }

    /**
     * Endpoint para deletar um negócio.
     * @param id O ID do negócio a ser deletado.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return Uma resposta vazia com status 204 No Content.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarNegocio(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        negocioService.deletarNegocio(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}

