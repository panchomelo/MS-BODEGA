package com.example.ms_reporte.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_reporte.dto.ReporteRequestDTO;
import com.example.ms_reporte.dto.UsuarioResponseDTO;
import com.example.ms_reporte.model.Reporte;
import com.example.ms_reporte.repository.ReporteRepository;

@ExtendWith(MockitoExtension.class)
class ReporteServiceTest {

    @Mock
    private ReporteRepository reporteRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @InjectMocks
    private ReporteService reporteService;

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe en ms-usuario")
    void crearDebeFallarCuandoUsuarioNoExiste() {
        Long usuarioId = 666L;

        ReporteRequestDTO request = new ReporteRequestDTO(
                "INVENTARIO_MENSUAL",
                LocalDateTime.now(),
                usuarioId,
                "https://bodega.com/reportes/inventario_2026_junio.pdf");

        // Configurar el WebClient para lanzar excepción al validar usuario
        when(webClient.get()
                .uri("/usuarios/{id}", usuarioId)
                .retrieve()
                .onStatus(any(), any())
                .bodyToMono(UsuarioResponseDTO.class)
                .block())
                .thenThrow(new RuntimeException("Usuario no existe en ms-usuario"));

        // Verificar que se lanza excepción y que nunca se intenta guardar
        assertThrows(RuntimeException.class, () -> reporteService.crear(request));
        verify(reporteRepository, never()).save(any(Reporte.class));
    }
}
