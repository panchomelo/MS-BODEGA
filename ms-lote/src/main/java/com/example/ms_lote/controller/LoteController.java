package com.example.ms_lote.controller;

import com.example.ms_lote.dto.LoteRequestDTO;
import com.example.ms_lote.dto.LoteResponseDTO;
import com.example.ms_lote.service.LoteService;

// Importaciones necesarias para Swagger/OpenAPI (IE 3.2.1)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/lotes")
@RequiredArgsConstructor
@Slf4j
// @Tag agrupa los endpoints en la interfaz visual de Swagger [1, 3]
@Tag(name = "Gestión de Lotes", description = "Endpoints para el control de lotes y fechas de vencimiento de productos")
public class LoteController {

    private final LoteService loteService;

    @Operation(summary = "Listar todos los lotes", description = "Retorna una lista completa de los lotes registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    @GetMapping
    public ResponseEntity<List<LoteResponseDTO>> listar() {
        log.info("Solicitando lista completa de lotes");
        return ResponseEntity.ok(loteService.listar());
    }

    @Operation(summary = "Obtener lote por ID", description = "Busca los detalles de un lote específico mediante su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lote encontrado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Lote no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> obtener(
            @Parameter(description = "ID del lote a consultar") @PathVariable Long id) {
        log.info("Buscando lote con ID: {}", id);
        return loteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo lote", description = "Registra un nuevo lote asociado a un producto, incluyendo cantidad y fecha de vencimiento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Lote creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping
    public ResponseEntity<LoteResponseDTO> crear(@Valid @RequestBody LoteRequestDTO dto) {
        log.info("Creando lote para producto ID: {}", dto.getProductoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loteService.crear(dto));
    }

    @Operation(summary = "Actualizar un lote", description = "Modifica los datos de un lote existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lote actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "El lote a actualizar no existe"),
        @ApiResponse(responseCode = "400", description = "Datos de actualización inválidos")
    })
    @PutMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteRequestDTO dto) {
        log.info("Actualizando lote ID: {}", id);
        return loteService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar un lote", description = "Elimina permanentemente un lote del registro")
    @ApiResponse(responseCode = "204", description = "Lote eliminado exitosamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando lote ID: {}", id);
        loteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}