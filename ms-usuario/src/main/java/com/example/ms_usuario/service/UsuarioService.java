package com.example.ms_usuario.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ms_usuario.dto.UsuarioRequestDTO;
import com.example.ms_usuario.dto.UsuarioResponseDTO;
import com.example.ms_usuario.model.Usuario;
import com.example.ms_usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public UsuarioResponseDTO registrar(UsuarioRequestDTO request) {
        log.info("Registrando nuevo usuario: {}", request.getUsername());
        
        // Validación de existencia previa (Integridad de datos)
        if (usuarioRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }

        Usuario usuario = Usuario.builder()
                .username(request.getUsername())
                .password(request.getPassword()) // Nota: En producción usar BCrypt
                .nombre(request.getNombre())
                .email(request.getEmail())
                .rol(request.getRol())
                .build();

        Usuario guardado = usuarioRepository.save(usuario);
        return mapToResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponseDTO> listarTodos() {
        log.info("Consultando lista completa de usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private UsuarioResponseDTO mapToResponse(Usuario u) {
        return UsuarioResponseDTO.builder()
                .id(u.getId())
                .username(u.getUsername())
                .nombre(u.getNombre())
                .email(u.getEmail())
                .rol(u.getRol())
                .build();
    }
}