package com.example.ms_usuario.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.ms_usuario.dto.UsuarioRequestDTO;
import com.example.ms_usuario.dto.UsuarioResponseDTO;
import com.example.ms_usuario.service.UsuarioService;

// Importaciones de OpenAPI (IE 3.2.1) [2, 4]
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
@Slf4j
// @Tag agrupa los endpoints bajo un nombre y descripción en la UI de Swagger [2, 5]
@Tag(name = "Gestión de Usuarios", description = "Endpoints para el registro y consulta de los usuarios del sistema de bodega")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @Operation(summary = "Registrar un nuevo usuario", description = "Crea un usuario en el sistema validando que los datos cumplan con las reglas de negocio [2]")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente [4]"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida o datos de usuario incorrectos [6]")
    })
    @PostMapping
    public ResponseEntity<UsuarioResponseDTO> registrar(@Valid @RequestBody UsuarioRequestDTO request) {
        log.info("Petición POST para crear usuario: {}", request.getUsername());
        UsuarioResponseDTO response = usuarioService.registrar(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los usuarios", description = "Retorna una lista completa de usuarios para fines de auditoría o administración [2]")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente [4]")
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> listarTodo() {
        log.info("Petición GET para listar todos los usuarios");
        return ResponseEntity.ok(usuarioService.listarTodos());
    }
}