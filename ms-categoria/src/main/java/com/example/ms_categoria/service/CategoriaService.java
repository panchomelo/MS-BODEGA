package com.example.ms_categoria.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional(readOnly = true)
    public List<CategoriaResponseDTO> obtenerTodas() {
        return categoriaRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponseDTO obtenerPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + id)); // IE 2.2.1 [8]
        return mapToResponse(categoria);
    }

    @Transactional
    public CategoriaResponseDTO crear(CategoriaRequestDTO request) {
        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre())
                .build();
        
        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoría guardada exitosamente con ID: {}", guardada.getId());
        return mapToResponse(guardada);
    }

    // Método auxiliar de mapeo (IE 2.4.2) [7]
    private CategoriaResponseDTO mapToResponse(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .build();
    }
}