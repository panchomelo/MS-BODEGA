package com.example.ms_auditoria.service;

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

import com.example.ms_auditoria.dto.AuditoriaRequestDTO;
import com.example.ms_auditoria.dto.UsuarioResponseDTO;
import com.example.ms_auditoria.model.Auditoria;
import com.example.ms_auditoria.repository.AuditoriaRepository;

@ExtendWith(MockitoExtension.class)
class AuditoriaServiceTest {

    @Mock
    private AuditoriaRepository auditoriaRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    private WebClient webClient;

    @InjectMocks
    private AuditoriaService auditoriaService;

    @Test
    @DisplayName("Debe lanzar excepción si el usuario no existe en ms-usuario")
    void crearDebeFallarCuandoUsuarioNoExiste() {
        Long usuarioId = 888L;

        AuditoriaRequestDTO request = new AuditoriaRequestDTO(
                usuarioId,
                "INSERT",
                "productos",
                123L,
                LocalDateTime.now(),
                "Se insertó un nuevo producto con ID 123");

        // Configurar el WebClient para lanzar excepción al validar usuario
        when(webClient.get()
                .uri("/usuarios/{id}", usuarioId)
                .retrieve()
                .onStatus(any(), any())
                .bodyToMono(UsuarioResponseDTO.class)
                .block())
                .thenThrow(new RuntimeException("Usuario no existe en ms-usuario"));

        // Verificar que se lanza excepción y que nunca se intenta guardar
        assertThrows(RuntimeException.class, () -> auditoriaService.crear(request));
        verify(auditoriaRepository, never()).save(any(Auditoria.class));
    }
}
