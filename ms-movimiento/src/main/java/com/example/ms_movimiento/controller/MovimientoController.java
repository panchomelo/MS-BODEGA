package com.example.ms_movimiento.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_movimiento.dto.MovimientoRequestDTO;
import com.example.ms_movimiento.dto.MovimientoResponseDTO;
import com.example.ms_movimiento.service.MovimientoService;

// Importaciones para Swagger/OpenAPI (Requisito IE 3.2.1)
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
@Slf4j
// @Tag agrupa los endpoints en la interfaz visual de Swagger [5, 6]
@Tag(name = "Gestión de Movimientos", description = "Endpoints para registrar entradas/salidas y consultar el historial de stock")
public class MovimientoController {

    private final MovimientoService movimientoService;

    @Operation(summary = "Registrar un nuevo movimiento", 
               description = "Crea un registro de movimiento y actualiza el stock en el ms-inventario de forma sincronizada")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movimiento registrado y stock actualizado con éxito"),
        @ApiResponse(responseCode = "400", description = "Error en los datos de entrada o stock insuficiente")
    })
    @PostMapping
    public ResponseEntity<String> registrar(@Valid @RequestBody MovimientoRequestDTO request) {
        log.info("Petición POST: Registrar {} para producto ID: {}",
                 request.getTipo(), request.getProductoId());
        
        movimientoService.registrarMovimiento(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body("Movimiento registrado y stock actualizado con éxito");
    }

    @Operation(summary = "Listar historial de movimientos", 
               description = "Retorna una lista completa de todos los movimientos registrados en la base de datos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista obtenida correctamente")
    })
    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listarTodo() {
        log.info("Petición GET: Listar historial de movimientos");
        return ResponseEntity.ok(movimientoService.listarTodo());
    }
}