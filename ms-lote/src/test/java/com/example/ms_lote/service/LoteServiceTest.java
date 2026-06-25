package com.example.ms_lote.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_lote.dto.LoteRequestDTO;
import com.example.ms_lote.dto.LoteResponseDTO;
import com.example.ms_lote.model.Lote;
import com.example.ms_lote.repository.LoteRepository;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class LoteServiceTest {

    @Mock
    private LoteRepository loteRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private LoteService loteService;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @Test
    @DisplayName("IE 3.1.1: Debería crear un lote exitosamente cuando el producto es válido")
    void crearLoteExitoso() {

        LoteRequestDTO request = new LoteRequestDTO(
                1L,
                "LOT001",
                100,
                100,
                LocalDateTime.now().plusMonths(6)
        );

        Lote loteGuardado = new Lote(
                1L,
                1L,
                "LOT001",
                100,
                100,
                LocalDateTime.now(),
                LocalDateTime.now().plusMonths(6)
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(Class.class)))
                .thenReturn(Mono.just(new Object()));

        when(loteRepository.save(any(Lote.class)))
                .thenReturn(loteGuardado);

        LoteResponseDTO resultado = loteService.crear(request);

        assertNotNull(resultado);
        verify(loteRepository, times(1)).save(any(Lote.class));
    }

    @Test
    @DisplayName("IE 3.1.2: Debería fallar al crear lote si el producto no existe en el sistema")
    void crearLoteErrorProducto() {

        LoteRequestDTO request = new LoteRequestDTO(
                99L,
                "LOT999",
                100,
                100,
                LocalDateTime.now().plusMonths(6)
        );

        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(Class.class)))
                .thenReturn(Mono.error(new RuntimeException("Producto no encontrado")));

        assertThrows(RuntimeException.class,
                () -> loteService.crear(request));

        verify(loteRepository, never()).save(any());
    }

    @Test
    @DisplayName("IE 3.1.1: Debería listar todos los lotes correctamente")
    void listarLotes() {

        when(loteRepository.findAll())
                .thenReturn(List.of(new Lote(), new Lote()));

        List<LoteResponseDTO> lista = loteService.listar();

        assertEquals(2, lista.size());
        verify(loteRepository, times(1)).findAll();
    }
}