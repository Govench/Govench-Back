package com.upao.govench.govench.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler extends RuntimeException{

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<String> handleResponseStatusException(ResponseStatusException ex) {
        // Retorna solo el mensaje de error con el status adecuado
        return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        // Retorna un mensaje genérico para otras excepciones
        return new ResponseEntity<>("Ocurrió un error inesperado.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}