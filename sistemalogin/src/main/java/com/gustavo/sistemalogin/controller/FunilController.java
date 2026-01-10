package com.gustavo.sistemalogin.controller;

import com.gustavo.sistemalogin.dto.FunilCreateDTO;
import com.gustavo.sistemalogin.dto.FunilResponseDTO;
import com.gustavo.sistemalogin.dto.FunilUpdateDTO;
import com.gustavo.sistemalogin.service.FunilService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gerir as operações CRUD de Funis.
 * Todos os endpoints aqui são protegidos e requerem um token JWT válido.
 */
@RestController
@RequestMapping("/api/funis")
public class FunilController {

    private final FunilService funilService;

    public FunilController(FunilService funilService) {
        this.funilService = funilService;
    }

    /**
     * Endpoint para criar um novo funil de vendas.
     * @param funilCreateDTO DTO com os dados para a criação do funil.
     * @param userDetails Detalhes do utilizador autenticado, injetados pelo Spring Security.
     * @return O funil recém-criado.
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASSISTENTE')")
    public ResponseEntity<FunilResponseDTO> criarFunil(@RequestBody FunilCreateDTO funilCreateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        FunilResponseDTO novoFunil = funilService.criarFunil(funilCreateDTO, userDetails.getUsername());
        return new ResponseEntity<>(novoFunil, HttpStatus.CREATED);
    }

    /**
     * Endpoint para listar todos os funis do utilizador autenticado.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return Uma lista com os funis do utilizador.
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASSISTENTE')")
    public ResponseEntity<List<FunilResponseDTO>> listarFunisDoUsuario(@AuthenticationPrincipal UserDetails userDetails) {
        List<FunilResponseDTO> funis = funilService.listarFunisDoUsuario(userDetails.getUsername());
        return ResponseEntity.ok(funis);
    }

    /**
     * Endpoint para buscar um funil específico pelo seu ID.
     * @param id O ID do funil a ser buscado.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return O funil encontrado.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASSISTENTE')")
    public ResponseEntity<FunilResponseDTO> buscarFunilPorId(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        FunilResponseDTO funil = funilService.buscarFunilPorId(id, userDetails.getUsername());
        return ResponseEntity.ok(funil);
    }

    /**
     * Endpoint para atualizar um funil existente.
     * @param id O ID do funil a ser atualizado.
     * @param funilUpdateDTO DTO com os dados a serem atualizados.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return O funil atualizado.
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASSISTENTE')")
    public ResponseEntity<FunilResponseDTO> atualizarFunil(@PathVariable Long id, @RequestBody FunilUpdateDTO funilUpdateDTO, @AuthenticationPrincipal UserDetails userDetails) {
        FunilResponseDTO funilAtualizado = funilService.atualizarFunil(id, funilUpdateDTO, userDetails.getUsername());
        return ResponseEntity.ok(funilAtualizado);
    }

    /**
     * Endpoint para deletar um funil.
     * @param id O ID do funil a ser deletado.
     * @param userDetails Detalhes do utilizador autenticado.
     * @return Uma resposta vazia com status 204 No Content.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR', 'ASSISTENTE')")
    public ResponseEntity<Void> deletarFunil(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        funilService.deletarFunil(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}

