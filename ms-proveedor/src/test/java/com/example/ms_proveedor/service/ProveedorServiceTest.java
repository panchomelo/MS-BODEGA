package com.example.ms_proveedor.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import com.example.ms_proveedor.dto.ProveedorRequestDTO;
import com.example.ms_proveedor.model.Proveedor;
import com.example.ms_proveedor.repository.ProveedorRepository;

@ExtendWith(MockitoExtension.class)
class ProveedorServiceTest {

    @Mock
    private ProveedorRepository repository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @InjectMocks
    private ProveedorService proveedorService;

    @Test
    @DisplayName("Debe lanzar excepción si el producto no existe en ms-producto")
    void crearDebeFallarCuandoProductoNoExiste() {
        Long productoId = 777L;

        ProveedorRequestDTO request = new ProveedorRequestDTO(
                "Proveedor ABC",
                productoId,
                "contacto@abc.com",
                "+57 300 1234567",
                "Cra 5 #10-20, Bogotá");

        // Configurar el WebClient para lanzar excepción al validar producto
        when(webClient.get()
                .uri("/{id}", productoId)
                .retrieve()
                .onStatus(any(), any())
                .bodyToMono(Object.class)
                .block())
                .thenThrow(new RuntimeException("Producto no encontrado en ms-producto"));

        // Verificar que se lanza excepción y que nunca se intenta guardar
        assertThrows(RuntimeException.class, () -> proveedorService.crear(request));
        verify(repository, never()).save(any(Proveedor.class));
    }
}
