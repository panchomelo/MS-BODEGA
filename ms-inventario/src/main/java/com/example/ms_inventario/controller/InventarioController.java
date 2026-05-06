package com.example.ms_inventario.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
@RequiredArgsConstructor
public class InventarioController {

    private final InventarioService service;

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crear(
            @Valid @RequestBody InventarioRequestDTO dto){

        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> listar(){
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<InventarioResponseDTO> obtenerPorProducto(
            @PathVariable Long productoId){

        return ResponseEntity.ok(service.obtenerPorProducto(productoId));
    }
}