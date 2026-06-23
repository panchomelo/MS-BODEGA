package com.example.ms_categoria.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_categoria.dto.CategoriaRequestDTO;
import com.example.ms_categoria.dto.CategoriaResponseDTO;
import com.example.ms_categoria.service.CategoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Gestión de Categorías", description = "Endpoints para la administración de categorías de productos") // IE 3.2.1 [4], [2]
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Obtener todas las categorías", description = "Lista todas las categorías registradas en la base de datos")
    @ApiResponse(responseCode = "200", description = "Lista obtenida con éxito")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        log.info("Petición recibida para listar todas las categorías"); // Trazabilidad técnica [2]
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    @Operation(summary = "Obtener una categoría por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("Buscando categoría con ID: {}", id);
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    @Operation(summary = "Crear una nueva categoría", description = "Registra una categoría con validación de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> guardar(@Valid @RequestBody CategoriaRequestDTO request) {
        log.info("Iniciando creación de categoría: {}", request.getNombre());
        CategoriaResponseDTO creada = categoriaService.crear(request);
        return new ResponseEntity<>(creada, HttpStatus.CREATED); // IE 2.4.1 [5], [6]
    }
}