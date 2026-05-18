package com.example.ms_lote.controller;

import com.example.ms_lote.dto.LoteRequestDTO;
import com.example.ms_lote.dto.LoteResponseDTO;
import com.example.ms_lote.service.LoteService;
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
public class LoteController {

    private final LoteService loteService;

    @GetMapping
    public ResponseEntity<List<LoteResponseDTO>> listar() {
        log.info("Solicitando lista completa de lotes");
        return ResponseEntity.ok(loteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> obtener(@PathVariable Long id) {
        log.info("Buscando lote con ID: {}", id);
        return loteService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<LoteResponseDTO> crear(@Valid @RequestBody LoteRequestDTO dto) {
        log.info("Creando lote para producto ID: {}", dto.getProductoId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(loteService.crear(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody LoteRequestDTO dto) {
        log.info("Actualizando lote ID: {}", id);
        return loteService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.info("Eliminando lote ID: {}", id);
        loteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
