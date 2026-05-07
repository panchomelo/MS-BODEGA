package com.example.ms_inventario.controller;

import java.util.List;

import org.springframework.http.HttpStatus; // Necesario para el código 201
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_inventario.dto.InventarioRequestDTO; // Requisito para trazabilidad
import com.example.ms_inventario.dto.InventarioResponseDTO;
import com.example.ms_inventario.service.InventarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/inventario")
@RequiredArgsConstructor
@Slf4j // Habilita los logs estructurados
public class InventarioController {

    private final InventarioService service;

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crear(@Valid @RequestBody InventarioRequestDTO dto) {
        // Trazabilidad: Log de inicio de operación [3]
        log.info("Recibida solicitud para registrar inventario del producto ID: {}", dto.getProductoId());
        
        // Mejora semántica: Retorno con 201 Created [3, 5]
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> listar() {
        log.info("Solicitando listado completo de inventario");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioResponseDTO> obtenerPorProducto(@PathVariable Long productoId) {
        log.info("Consultando inventario para el producto ID remoto: {}", productoId);
        return ResponseEntity.ok(service.obtenerPorProducto(productoId));
    }
}