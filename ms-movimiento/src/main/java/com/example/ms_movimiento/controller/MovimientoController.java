package com.example.ms_movimiento.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_movimiento.dto.MovimientoRequestDTO;
import com.example.ms_movimiento.dto.MovimientoResponseDTO;
import com.example.ms_movimiento.service.MovimientoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
@Slf4j
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<String> registrar(@Valid @RequestBody MovimientoRequestDTO request) {
        log.info("Petición POST: Registrar {} para producto ID: {}",
                 request.getTipo(), request.getProductoId());
        
        movimientoService.registrarMovimiento(request);
        
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body("Movimiento registrado y stock actualizado con éxito");
    }

    @GetMapping
    public ResponseEntity<List<MovimientoResponseDTO>> listarTodo() {
        log.info("Petición GET: Listar historial de movimientos");
        return ResponseEntity.ok(movimientoService.listarTodo());
    }
}