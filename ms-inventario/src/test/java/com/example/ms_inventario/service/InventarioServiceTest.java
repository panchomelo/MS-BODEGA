package com.example.ms_inventario.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_inventario.dto.InventarioRequestDTO;
import com.example.ms_inventario.dto.ProductoDTO;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

        @Mock
        private InventarioRepository inventarioRepository;

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private WebClient webClient;

        @InjectMocks
        private InventarioService inventarioService;

        @Test
        @DisplayName("Debe rechazar un nuevo inventario si el productoId ya tiene un registro")
        void guardarDebeFallarCuandoProductoIdYaExiste() {
                Long productoId = 100L;

                InventarioRequestDTO request = InventarioRequestDTO.builder()
                                .productoId(productoId)
                                .stock(5)
                                .build();

                Inventario inventarioExistente = Inventario.builder()
                                .id(1L)
                                .productoId(productoId)
                                .stock(10)
                                .build();

                ProductoDTO productoMock = ProductoDTO.builder()
                                .id(productoId)
                                .nombre("Producto falso")
                                .precio(100.0)
                                .categoriaId(1L)
                                .build();

                when(webClient.get()
                                .uri("/productos/{id}", productoId)
                                .retrieve()
                                .bodyToMono(ProductoDTO.class)
                                .block()).thenReturn(productoMock);

                when(inventarioRepository.findByProductoId(productoId)).thenReturn(Optional.of(inventarioExistente));

                assertThrows(RuntimeException.class, () -> inventarioService.guardar(request));

                verify(inventarioRepository, times(1)).findByProductoId(productoId);
                verify(inventarioRepository, never()).save(any(Inventario.class));
        }
}