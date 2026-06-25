package com.example.ms_alerta.controller;

import com.example.ms_alerta.dto.AlertaRequestDTO;
import com.example.ms_alerta.dto.AlertaResponseDTO;
import com.example.ms_alerta.service.AlertaService;
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
@RequestMapping("/alertas")
@Tag(name = "Alertas", description = "Operaciones para gestionar alertas de inventario y notificaciones")
@RequiredArgsConstructor
@Slf4j
public class AlertaController {

    private final AlertaService alertaService;

    @Operation(summary = "Listar alertas", description = "Obtiene todas las alertas registradas en el sistema")
    @GetMapping
    public ResponseEntity<List<AlertaResponseDTO>> listar() {
        log.info("Solicitando lista de alertas");
        return ResponseEntity.ok(alertaService.listar());
    }

    @Operation(summary = "Obtener alerta por ID", description = "Recupera una alerta específica usando su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> obtenerPorId(@PathVariable Long id) {
        log.info("Buscando alerta con ID: {}", id);
        return alertaService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear alerta", description = "Crea una nueva alerta validando el producto y la lógica de negocio")
    @PostMapping
    public ResponseEntity<AlertaResponseDTO> crear(@Valid @RequestBody AlertaRequestDTO dto) {
        log.info("Creando alerta para producto ID: {}", dto.getProductoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(alertaService.crear(dto));
    }

    @Operation(summary = "Actualizar alerta", description = "Actualiza una alerta existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<AlertaResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AlertaRequestDTO dto) {
        log.info("Actualizando alerta ID: {}", id);
        return alertaService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar alerta", description = "Elimina una alerta por su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando alerta ID: {}", id);
        alertaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
