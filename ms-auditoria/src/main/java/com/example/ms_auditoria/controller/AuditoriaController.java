package com.example.ms_auditoria.controller;

import com.example.ms_auditoria.dto.AuditoriaRequestDTO;
import com.example.ms_auditoria.dto.AuditoriaResponseDTO;
import com.example.ms_auditoria.service.AuditoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auditorias")
@Tag(name = "Auditorias", description = "Operaciones para gestionar auditorías de la bodega")
@RequiredArgsConstructor
@Slf4j
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @Operation(summary = "Listar auditorías", description = "Obtiene todas las auditorías registradas en el sistema")
    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDTO>> listar() {
        log.info("Solicitando lista completa de auditorias");
        return ResponseEntity.ok(auditoriaService.listar());
    }

    @Operation(summary = "Obtener auditoría por ID", description = "Recupera una auditoría específica usando su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> obtener(@PathVariable Long id) {
        log.info("Buscando auditoria ID: {}", id);
        return auditoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear auditoría", description = "Crea una nueva auditoría para un usuario específico")
    @PostMapping
    public ResponseEntity<AuditoriaResponseDTO> crear(@Valid @RequestBody AuditoriaRequestDTO dto) {
        log.info("Creando auditoria para usuario ID: {}", dto.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(auditoriaService.crear(dto));
    }

    @Operation(summary = "Actualizar auditoría", description = "Actualiza una auditoría existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AuditoriaRequestDTO dto) {
        log.info("Actualizando auditoria ID: {}", id);
        return auditoriaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar auditoría", description = "Elimina una auditoría por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando auditoria ID: {}", id);
        auditoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
