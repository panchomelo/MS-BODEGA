package com.example.ms_usuario.service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_usuario.dto.UsuarioRequestDTO;
import com.example.ms_usuario.dto.UsuarioResponseDTO;
import com.example.ms_usuario.model.Usuario;
import com.example.ms_usuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class) // IE 3.1.1: Habilita Mockito para aislamiento total [5]
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository; // Doble de prueba para el repositorio [1]

    @InjectMocks
    private UsuarioService usuarioService; // Inyecta los mocks en el servicio

    @Test
    @DisplayName("IE 3.1.1: Debería registrar un usuario exitosamente (Camino Feliz)")
    void registrarUsuarioExitoso() {
        // GIVEN (Arrange): Configuración del estado inicial y comportamiento de mocks [4]
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .username("fuyu.dev")
                .password("pass123")
                .nombre("Fuyu")
                .email("fuyu@example.com")
                .rol("ADMIN")
                .build();

        Usuario usuarioGuardado = Usuario.builder()
                .id(1L)
                .username("fuyu.dev")
                .build();

        // Simulamos que el nombre de usuario NO existe en la BD
        when(usuarioRepository.findByUsername("fuyu.dev")).thenReturn(Optional.empty());
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioGuardado);

        // WHEN (Act): Ejecución del método de lógica de negocio [4]
        UsuarioResponseDTO resultado = usuarioService.registrar(request);

        // THEN (Assert): Verificación del resultado y comportamiento [4]
        assertNotNull(resultado);
        assertEquals("fuyu.dev", resultado.getUsername());
        verify(usuarioRepository, times(1)).save(any(Usuario.class)); // Verifica persistencia
    }

    @Test
    @DisplayName("IE 3.1.2: Debería lanzar excepción si el nombre de usuario ya está en uso")
    void registrarUsuarioErrorDuplicado() {
        // GIVEN: El usuario ya existe en el sistema
        UsuarioRequestDTO request = UsuarioRequestDTO.builder()
                .username("admin")
                .build();

        when(usuarioRepository.findByUsername("admin")).thenReturn(Optional.of(new Usuario()));

        // WHEN & THEN: Verificación de la regla de negocio y protección de integridad [6]
        assertThrows(RuntimeException.class, () -> usuarioService.registrar(request));
        
        // Verificamos que NUNCA se intentó guardar en la BD tras el error
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("IE 3.1.1: Debería listar todos los usuarios correctamente")
    void listarUsuarios() {
        // GIVEN
        List<Usuario> listaMock = List.of(
            Usuario.builder().id(1L).username("user1").build(),
            Usuario.builder().id(2L).username("user2").build()
        );
        when(usuarioRepository.findAll()).thenReturn(listaMock);

        // WHEN
        List<UsuarioResponseDTO> resultado = usuarioService.listarTodos();

        // THEN
        assertEquals(2, resultado.size());
        verify(usuarioRepository, times(1)).findAll();
    }
}