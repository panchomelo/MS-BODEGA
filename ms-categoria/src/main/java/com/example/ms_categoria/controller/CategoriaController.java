package com.example.ms_categoria.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_categoria.dto.CategoriaRequestDTO;
import com.example.ms_categoria.dto.CategoriaResponseDTO;
import com.example.ms_categoria.service.CategoriaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService categoriaService;

    // Obtener todas las categorías
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> listarTodas() {
        return ResponseEntity.ok(categoriaService.obtenerTodas());
    }

    // Obtener una categoría por su ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(categoriaService.obtenerPorId(id));
    }

    // Crear una nueva categoría con validación
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> guardar(@Valid @RequestBody CategoriaRequestDTO request) {
        CategoriaResponseDTO creada = categoriaService.crear(request);
        return new ResponseEntity<>(creada, HttpStatus.CREATED); // Retorna 201 Created
    }
}