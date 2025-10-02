package com.gustavo.sistemalogin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exceção personalizada para ser lançada quando um recurso não é encontrado na base de dados.
 * A anotação @ResponseStatus faz com que o Spring retorne o status HTTP 404 Not Found.
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
