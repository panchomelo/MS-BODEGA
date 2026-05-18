package com.example.ms_auditoria.controller;

import com.example.ms_auditoria.dto.AuditoriaRequestDTO;
import com.example.ms_auditoria.dto.AuditoriaResponseDTO;
import com.example.ms_auditoria.service.AuditoriaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/auditorias")
@RequiredArgsConstructor
@Slf4j
public class AuditoriaController {

    private final AuditoriaService auditoriaService;

    @GetMapping
    public ResponseEntity<List<AuditoriaResponseDTO>> listar() {
        log.info("Solicitando lista completa de auditorias");
        return ResponseEntity.ok(auditoriaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> obtener(@PathVariable Long id) {
        log.info("Buscando auditoria ID: {}", id);
        return auditoriaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AuditoriaResponseDTO> crear(@Valid @RequestBody AuditoriaRequestDTO dto) {
        log.info("Creando auditoria para usuario ID: {}", dto.getUsuarioId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(auditoriaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditoriaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AuditoriaRequestDTO dto) {
        log.info("Actualizando auditoria ID: {}", id);
        return auditoriaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando auditoria ID: {}", id);
        auditoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
