package com.example.ms_alerta.controller;

import com.example.ms_alerta.dto.AlertaRequestDTO;
import com.example.ms_alerta.dto.AlertaResponseDTO;
import com.example.ms_alerta.service.AlertaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/alertas")
@RequiredArgsConstructor
@Slf4j
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping
    public ResponseEntity<List<AlertaResponseDTO>> listar() {
        log.info("Solicitando lista de alertas");
        return ResponseEntity.ok(alertaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Buscando alerta con ID: {}", id);
        return alertaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AlertaResponseDTO> crear(@Valid @RequestBody AlertaRequestDTO dto) {
        log.info("Creando alerta para producto ID: {}", dto.getProductoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alertaService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlertaRequestDTO dto) {
        log.info("Actualizando alerta ID: {}", id);
        return alertaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando alerta ID: {}", id);
        alertaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
