package com.example.ms_categoria.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import com.example.ms_categoria.dto.CategoriaRequestDTO;
import com.example.ms_categoria.dto.CategoriaResponseDTO;
import com.example.ms_categoria.model.Categoria;
import com.example.ms_categoria.repository.CategoriaRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public List<CategoriaResponseDTO> obtenerTodas() {
        log.info("Consultando todas las categorías");
        return categoriaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CategoriaResponseDTO crear(CategoriaRequestDTO request) {
        log.info("Creando nueva categoría: {}", request.getNombre());
        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();
        
        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoría guardada exitosamente con ID: {}", guardada.getId());
        return mapToResponse(guardada);
    }

    public CategoriaResponseDTO obtenerPorId(Long id) {
        log.info("Buscando categoría con ID: {}", id);
        return categoriaRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    log.error("Error: No se encontró la categoría con ID: {}", id);
                    return new RuntimeException("Categoría no encontrada");
                });
    }

    private CategoriaResponseDTO mapToResponse(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}