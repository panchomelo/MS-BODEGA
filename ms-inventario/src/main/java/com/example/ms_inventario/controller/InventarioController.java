package com.example.ms_inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // Para IE 2.2.2

import com.example.ms_inventario.dto.InventarioRequestDTO;
import com.example.ms_inventario.dto.InventarioResponseDTO;
import com.example.ms_inventario.service.InventarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventario") // Ruta base definida en tus fuentes [2]
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    // 1. Obtener todo el stock actual (IE 2.1.2)
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> listarTodo() {
        return ResponseEntity.ok(inventarioService.listarTodo());
    }

    // 2. Endpoint para actualizar stock (IE 2.4.1 y IE 2.2.1)
    // Este método permite procesar entradas y salidas (rebaja de pollo)
    @PostMapping
    public ResponseEntity<InventarioResponseDTO> guardar(@Valid @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequestDTO dto) {
        return inventarioService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}