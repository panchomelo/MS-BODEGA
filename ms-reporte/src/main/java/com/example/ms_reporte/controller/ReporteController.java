package com.example.ms_reporte.controller;

import com.example.ms_reporte.dto.ReporteRequestDTO;
import com.example.ms_reporte.dto.ReporteResponseDTO;
import com.example.ms_reporte.service.ReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
@Slf4j
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public ResponseEntity<List<ReporteResponseDTO>> listar() {
        log.info("Solicitando lista completa de reportes");
        return ResponseEntity.ok(reporteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> obtener(@PathVariable Long id) {
        log.info("Buscando reporte ID: {}", id);
        return reporteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReporteResponseDTO> crear(@Valid @RequestBody ReporteRequestDTO dto) {
        log.info("Creando reporte tipo: {}", dto.getTipoReporte());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reporteService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ReporteRequestDTO dto) {
        log.info("Actualizando reporte ID: {}", id);
        return reporteService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando reporte ID: {}", id);
        reporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
