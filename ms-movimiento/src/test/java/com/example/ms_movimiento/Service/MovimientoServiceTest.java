package com.example.ms_movimiento.service;

import java.util.function.Function;
import java.util.function.Predicate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_movimiento.dto.MovimientoRequestDTO;
import com.example.ms_movimiento.model.Movimiento;
import com.example.ms_movimiento.repository.MovimientoRepository;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private MovimientoService movimientoService;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    @BeforeEach
    void setUp() {}

    @Test
    @DisplayName("IE 3.1.1: Debería registrar un movimiento exitosamente (Camino Feliz)")
    void registrarMovimientoExitoso() {
        MovimientoRequestDTO request = MovimientoRequestDTO.builder()
                .productoId(1L)
                .cantidad(10)
                .tipo("ENTRADA")
                .build();

        // Mock para el GET (Validación de Producto)
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(Predicate.class), any(Function.class))).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(new Object()));

        // Mock para el POST (Actualización de Inventario)
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        // Nota: requestHeadersSpec.retrieve() y responseSpec.onStatus ya están cubiertos arriba

        when(responseSpec.bodyToMono(Void.class)).thenReturn(Mono.empty());

        movimientoService.registrarMovimiento(request);

        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("IE 3.1.1: Debería lanzar excepción si el producto no existe")
    void registrarMovimientoErrorProducto() {
        MovimientoRequestDTO request = MovimientoRequestDTO.builder()
                .productoId(99L)
                .cantidad(5)
                .tipo("SALIDA")
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(Predicate.class), any(Function.class))).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.error(new RuntimeException("Error: El producto con ID 99 no existe")));

        assertThrows(RuntimeException.class, () -> {
            movimientoService.registrarMovimiento(request);
        });

        verify(movimientoRepository, never()).save(any());
    }
}