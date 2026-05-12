package com.example.ms_inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController; // Para IE 2.2.2

import com.example.ms_inventario.dto.InventarioRequestDTO;
import com.example.ms_inventario.dto.InventarioResponseDTO;
import com.example.ms_inventario.service.InventarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/inventario") // Ruta base definida en tus fuentes [2]
@RequiredArgsConstructor
@Slf4j // IE 2.3.2: Trazabilidad de peticiones
public class InventarioController {

    private final InventarioService inventarioService;

    // 1. Obtener todo el stock actual (IE 2.1.2)
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> listarTodo() {
        log.info("Petición recibida para listar todo el inventario");
        return ResponseEntity.ok(inventarioService.listarTodo());
    }

    // 2. Endpoint para actualizar stock (IE 2.4.1 y IE 2.2.1)
    // Este método permite procesar entradas y salidas (rebaja de pollo)
    @PostMapping("/actualizar")
    public ResponseEntity<String> actualizarStock(@Valid @RequestBody InventarioRequestDTO request) {
        log.info("Petición de actualización de stock para producto ID: {}", request.getProductoId());
        
        // El servicio se encarga de validar el producto en ms-producto [3]
        inventarioService.actualizarStock(request.getProductoId(), request.getStock());
        
        return ResponseEntity.ok("Operación de stock procesada exitosamente");
    }
}