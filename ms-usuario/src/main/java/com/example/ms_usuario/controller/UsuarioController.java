package com.example.ms_usuario.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ms_usuario.dto.UsuarioRequestDTO;
import com.example.ms_usuario.dto.UsuarioResponseDTO;
import com.example.ms_usuario.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO request) {
        log.info("Petición POST para crear usuario: {}", request.getUsername());
        UsuarioResponseDTO response = usuarioService.registrar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodo() {
        log.info("Petición GET para listar todos los usuarios");
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}