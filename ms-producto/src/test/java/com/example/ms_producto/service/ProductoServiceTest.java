package com.example.ms_producto.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

    @Mock
    private ProductoRepository repository; // Simula la persistencia en Oracle Cloud

    @Mock
    private WebClient webClient; // Simula la llamada remota a ms-categoria

    @InjectMocks
    private ProductoService service; // Inyecta los mocks en el servicio real

    // Mocks auxiliares para emular el comportamiento fluido del WebClient
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

    // ==========================================
    // METODO: CREAR (POST)
    // ==========================================

    @Test
    @DisplayName("POST - Debería crear un producto exitosamente si la categoría es válida")
    void crearProductoExitoso() {
        mockWebClientSuccess();
        when(repository.save(any(Producto.class))).thenReturn(producto);

        ProductoResponseDTO respuesta = service.crear(requestDTO);

        assertNotNull(respuesta);
        assertEquals("Monitor Pro", respuesta.getNombre());
        verify(repository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("POST - Debería lanzar excepción cuando ms-categoria no encuentra el ID")
    void crearProductoErrorCategoria() {
        mockWebClientError();

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.crear(requestDTO);
        });

        assertTrue(exception.getMessage().contains("Categoría no encontrada"));
        verify(repository, never()).save(any(Producto.class));
    }

    // ==========================================
    // METODO: LISTAR Y OBTENER (GET)
    // ==========================================

    @Test
    @DisplayName("GET - Debería retornar la lista completa de productos")
    void listarProductosExitoso() {
        when(repository.findAll()).thenReturn(List.of(producto));

        List<ProductoResponseDTO> resultado = service.listar();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("Monitor Pro", resultado.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    @Test
    @DisplayName("GET - Debería retornar un producto específico cuando el ID existe")
    void obtenerPorIdExitoso() {
        when(repository.findById(1L)).thenReturn(Optional.of(producto));

        ProductoResponseDTO resultado = service.obtenerPorId(1L);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("GET - Debería lanzar excepción si el producto buscado no existe")
    void obtenerPorIdNoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.obtenerPorId(99L);
        });

        verify(repository, times(1)).findById(99L);
    }

    // ==========================================
    // METODO: ACTUALIZAR (PUT)
    // ==========================================

    @Test
    @DisplayName("PUT - Debería actualizar un producto existente de forma exitosa")
    void actualizarProductoExitoso() {
        // GIVEN: El producto original existe, la nueva categoría es válida y se guarda el cambio
        mockWebClientSuccess();
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        when(repository.save(any(Producto.class))).thenReturn(producto);

        ProductoRequestDTO updateDTO = new ProductoRequestDTO("Monitor Pro Modificado", 380000.0, 1L);

        // WHEN: Ejecutamos la actualización
        ProductoResponseDTO resultado = service.actualizar(1L, updateDTO);

        // THEN: Verificamos llamadas e integridad
        assertNotNull(resultado);
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).save(any(Producto.class));
    }

    @Test
    @DisplayName("PUT - Debería lanzar excepción al actualizar si el producto no existe")
    void actualizarProductoNoExistente() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        ProductoRequestDTO updateDTO = new ProductoRequestDTO("Monitor Pro Modificado", 380000.0, 1L);

        assertThrows(RuntimeException.class, () -> {
            service.actualizar(99L, updateDTO);
        });

        verify(repository, times(1)).findById(99L);
        verify(repository, never()).save(any(Producto.class));
    }

    // ==========================================
    // METODO: ELIMINAR (DELETE)
    // ==========================================

    @Test
    @DisplayName("DELETE - Debería eliminar el producto de la base de datos si el ID existe")
    void eliminarProductoExitoso() {
        // GIVEN: El método primero verifica existencia del producto
        when(repository.findById(1L)).thenReturn(Optional.of(producto));
        doNothing().when(repository).delete(any(Producto.class));

        // WHEN: Se solicita la eliminación
        assertDoesNotThrow(() -> service.eliminar(1L));

        // THEN: Confirmamos que se buscó y se borró exactamente una vez
        verify(repository, times(1)).findById(1L);
        verify(repository, times(1)).delete(any(Producto.class));
    }

    @Test
    @DisplayName("DELETE - Debería lanzar excepción si se intenta eliminar un ID inexistente")
    void eliminarProductoNoEncontrado() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            service.eliminar(99L);
        });

        verify(repository, times(1)).findById(99L);
        verify(repository, never()).delete(any(Producto.class));
    }

    // ==========================================
    // METODOS DE SOPORTE (WEBCLIENT MOCKS)
    // ==========================================

    private void mockWebClientSuccess() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.just(new Object()));
    }

    private void mockWebClientError() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString(), anyLong())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(any(Class.class))).thenReturn(Mono.error(new RuntimeException("Categoría no encontrada")));
    }
}