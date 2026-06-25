package com.example.ms_inventario.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_inventario.dto.InventarioRequestDTO;
import com.example.ms_inventario.dto.InventarioResponseDTO;
import com.example.ms_inventario.service.InventarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/inventario")
@Tag(name = "Inventario", description = "Operaciones para gestionar el inventario de productos en bodega")
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService inventarioService;

    @Operation(summary = "Listar inventario", description = "Obtiene todo el stock actual de la bodega")
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> listarTodo() {
        return ResponseEntity.ok(inventarioService.listarTodo());
    }

    // Endpoint para crear un nuevo inventario y validar el producto existente en
    // ms-producto
    @Operation(summary = "Registrar inventario", description = "Registra un nuevo inventario validando el producto en ms-producto")
    @PostMapping
    public ResponseEntity<InventarioResponseDTO> guardar(@Valid @RequestBody InventarioRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(inventarioService.guardar(dto));
    }

    @Operation(summary = "Actualizar inventario", description = "Actualiza el stock de un inventario existente por su ID")
    @PutMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody InventarioRequestDTO dto) {
        return inventarioService.actualizar(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}