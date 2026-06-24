package com.example.ms_proveedor.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_proveedor.dto.ProveedorRequestDTO;
import com.example.ms_proveedor.dto.ProveedorResponseDTO;
import com.example.ms_proveedor.service.ProveedorService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/proveedores")
@Tag(name = "Proveedores", description = "Operaciones para gestionar proveedores de la bodega")
@RequiredArgsConstructor
@Slf4j
public class ProveedorController {

    private final ProveedorService service;

    @Operation(summary = "Crear proveedor", description = "Registra un nuevo proveedor en el sistema")
    @PostMapping
    public ResponseEntity<ProveedorResponseDTO> crear(@Valid @RequestBody ProveedorRequestDTO dto) {
        log.info("Solicitud para crear proveedor: {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @Operation(summary = "Listar proveedores", description = "Obtiene el listado completo de proveedores registrados")
    @GetMapping
    public ResponseEntity<List<ProveedorResponseDTO>> listar() {
        log.info("Solicitando lista completa de proveedores");
        return ResponseEntity.ok(service.listar());
    }

    @Operation(summary = "Obtener proveedor por ID", description = "Recupera un proveedor usando su identificador")
    @GetMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> obtener(@PathVariable Long id) {
        log.info("Buscando proveedor ID: {}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @Operation(summary = "Actualizar proveedor", description = "Actualiza la información de un proveedor existente")
    @PutMapping("/{id}")
    public ResponseEntity<ProveedorResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProveedorRequestDTO dto) {
        log.info("Solicitud para actualizar proveedor ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @Operation(summary = "Eliminar proveedor", description = "Elimina un proveedor usando su ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Eliminando proveedor ID: {}", id);
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
