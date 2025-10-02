package com.gustavo.sistemalogin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Classe centralizada para tratamento de exceções em toda a aplicação.
 * Captura exceções específicas e as transforma em respostas JSON padronizadas.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura a nossa exceção personalizada ResourceNotFoundException.
     * @return Uma resposta JSON formatada com o status 404 Not Found.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.NOT_FOUND.value());
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    /**
     * Captura a exceção de segurança quando um utilizador tenta aceder a um recurso que não lhe pertence.
     * @return Uma resposta JSON formatada com o status 403 Forbidden.
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<Object> handleSecurityException(SecurityException ex, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.FORBIDDEN.value());
        body.put("error", "Forbidden");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false).replace("uri=", ""));

        return new ResponseEntity<>(body, HttpStatus.FORBIDDEN);
    }

    // Pode adicionar outros métodos @ExceptionHandler para outras exceções aqui (ex: IllegalArgumentException para 400 Bad Request)
}
