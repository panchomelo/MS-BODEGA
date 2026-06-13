package com.example.ms_producto.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_producto.dto.ProductoRequestDTO;
import com.example.ms_producto.dto.ProductoResponseDTO;
import com.example.ms_producto.model.Producto;
import com.example.ms_producto.repository.ProductoRepository;

import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class) // Habilita Mockito para JUnit 5 [3]
public class ProductoServiceTest {

    @Mock
    private ProductoRepository repository; // Simula la persistencia en Oracle [3, 5]

    @Mock
    private WebClient webClient; // Simula la interoperabilidad remota [5, 6]

    @InjectMocks
    private ProductoService service; // Inyecta los mocks automáticamente en el servicio real

    // Mocks auxiliares para manejar la API fluida de WebClient
    @Mock private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private Producto producto;
    private ProductoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        producto = Producto.builder()
                .id(1L)
                .nombre("Monitor Pro")
                .precio(350000.0)
                .categoriaId(1L)
                .build();

        requestDTO = new ProductoRequestDTO("Monitor Pro", 350000.0, 1L);
    }

    @Test
    @DisplayName("Debería crear un producto exitosamente (Caso Feliz)") // [7, 8]
    void crearProductoExitoso() {
        // GIVEN (Arrange): Configuración del estado inicial y comportamiento de mocks [7, 9]
        mockWebClientSuccess(); // Simulamos que la categoría existe en ms-categoria
        when(repository.save(any(Producto.class))).thenReturn(producto);

        // WHEN (Act): Ejecución de la unidad de código [7]
        ProductoResponseDTO respuesta = service.crear(requestDTO);

        // THEN (Assert): Verificación de resultados y comportamiento [2, 7]
        assertNotNull(respuesta);
        assertEquals("Monitor Pro", respuesta.getNombre());
        verify(repository, times(1)).save(any(Producto.class)); // Verifica persistencia real [2]
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando la categoría no es válida") // [8]
    void crearProductoErrorCategoria() {
        // GIVEN: Simulamos que ms-categoria devuelve un error (interoperabilidad fallida) [10]
        mockWebClientError();

        // WHEN & THEN (Act & Assert): Verificamos que se lance la excepción esperada [11]
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.crear(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Categoría no encontrada"));
        verify(repository, never()).save(any(Producto.class)); // Garantiza integridad de datos [12]
    }

    /**
     * Métodos de soporte para simular la cadena de WebClient
     */
    private void mockWebClientSuccess() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Object.class)).thenReturn(Mono.just(new Object()));
    }

    private void mockWebClientError() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenThrow(new RuntimeException("Categoría no encontrada"));
    }
}