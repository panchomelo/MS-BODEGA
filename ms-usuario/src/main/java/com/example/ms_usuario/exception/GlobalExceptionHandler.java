package com.example.ms_usuario.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Clase para el manejo global de excepciones en el microservicio de usuarios (IE 2.3.1).
 * Centraliza las respuestas de error para asegurar la consistencia del sistema.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura errores de validación en los DTOs (ej: username corto o email inválido).
     * Mapea cada campo con su respectivo mensaje de error.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage()));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Captura excepciones de lógica de negocio, como cuando un usuario ya existe.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeExceptions(RuntimeException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        
        // Define el código de estado: 404 si no se encuentra, 400 para otros errores de negocio
        HttpStatus status = ex.getMessage().contains("no existe") ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        
        return new ResponseEntity<>(error, status);
    }
}