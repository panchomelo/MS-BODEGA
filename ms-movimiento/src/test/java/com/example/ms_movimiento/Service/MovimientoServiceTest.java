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
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private WebClient webClient;

    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock private WebClient.RequestBodySpec requestBodySpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private MovimientoService movimientoService;

    @Test
    void registrarMovimientoExitoso() {

        MovimientoRequestDTO request = MovimientoRequestDTO.builder()
                .productoId(1L)
                .cantidad(10)
                .tipo("ENTRADA")
                .build();

        // ===== GET PRODUCTO =====
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.just("OK"));

        // ===== POST INVENTARIO =====
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Void.class))
                .thenReturn(Mono.empty());

        // EXECUTE
        movimientoService.registrarMovimiento(request);

        // VERIFY
        verify(movimientoRepository, times(1))
                .save(any(Movimiento.class));
    }

    @Test
    void registrarMovimientoErrorProducto() {

        MovimientoRequestDTO request = MovimientoRequestDTO.builder()
                .productoId(99L)
                .cantidad(5)
                .tipo("SALIDA")
                .build();

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(String.class))
                .thenReturn(Mono.error(new RuntimeException("Producto no existe")));

        assertThrows(RuntimeException.class,
                () -> movimientoService.registrarMovimiento(request));

        verify(movimientoRepository, never()).save(any());
    }
}