package com.example.ms_categoria.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_categoria.dto.CategoriaRequestDTO;
import com.example.ms_categoria.dto.CategoriaResponseDTO;
import com.example.ms_categoria.model.Categoria;
import com.example.ms_categoria.repository.CategoriaRepository;

@ExtendWith(MockitoExtension.class) // Habilita Mockito para JUnit 5 [4]
public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository; // Simulación del repositorio [5], [3]

    @InjectMocks
    private CategoriaService categoriaService; // Inyecta los mocks en el servicio

    private Categoria categoria;
    private CategoriaRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // Configuración inicial para los tests [2]
        categoria = Categoria.builder()
                .id(1L)
                .nombre("Electrónica")
                .build();

        requestDTO = CategoriaRequestDTO.builder()
                .nombre("Electrónica")
                .build();
    }

    @Test
    @DisplayName("Debería listar todas las categorías exitosamente")
    void listarTodasExitoso() {
        
        when(categoriaRepository.findAll()).thenReturn(Arrays.asList(categoria));

        
        List<CategoriaResponseDTO> resultado = categoriaService.obtenerTodas();

        
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Electrónica", resultado.get(0).getNombre());
        verify(categoriaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debería obtener una categoría por ID")
    void obtenerPorIdExitoso() {
        
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        
        CategoriaResponseDTO resultado = categoriaService.obtenerPorId(1L);

        
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(categoriaRepository).findById(1L);
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la categoría no existe")
    void obtenerPorIdNoEncontrado() {
        
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        
        assertThrows(RuntimeException.class, () -> {
            categoriaService.obtenerPorId(99L);
        });
    }

    @Test
    @DisplayName("Debería crear una nueva categoría exitosamente")
    void crearCategoriaExitoso() {
        
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        
        CategoriaResponseDTO resultado = categoriaService.crear(requestDTO);

        
        assertNotNull(resultado);
        assertEquals("Electrónica", resultado.getNombre());
        verify(categoriaRepository).save(any(Categoria.class)); // Verifica que se llamó al repo [3]
    }
}