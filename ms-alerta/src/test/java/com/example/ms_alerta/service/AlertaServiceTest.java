package com.example.ms_alerta.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_alerta.dto.AlertaRequestDTO;
import com.example.ms_alerta.dto.ProductoResponseDTO;
import com.example.ms_alerta.model.Alerta;
import com.example.ms_alerta.repository.AlertaRepository;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class AlertaServiceTest {

    @Mock
    private AlertaRepository alertaRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @InjectMocks
    private AlertaService alertaService;

    @Test
    @DisplayName("Debe lanzar excepción si el producto no existe en ms-producto")
    void crearDebeFallarCuandoProductoNoExiste() {
        Long productoId = 999L;

        AlertaRequestDTO request = new AlertaRequestDTO(
                productoId,
                "BAJO_STOCK",
                "ACTIVA",
                "Stock bajo para este producto");

        // Configurar el WebClient para lanzar excepción al validar producto
        when(webClient.get()
                .uri("/productos/{id}", productoId)
                .retrieve()
                .onStatus(any(), any())
                .bodyToMono(ProductoResponseDTO.class)
                .block())
                .thenThrow(new RuntimeException("Producto no existe en ms-producto"));

        // Verificar que se lanza excepción y que nunca se intenta guardar
        assertThrows(RuntimeException.class, () -> alertaService.crear(request));
        verify(alertaRepository, never()).save(any(Alerta.class));
    }
}
