package com.example.ms_auditoria.service;

import com.example.ms_auditoria.dto.AuditoriaRequestDTO;
import com.example.ms_auditoria.dto.AuditoriaResponseDTO;
import com.example.ms_auditoria.dto.UsuarioResponseDTO;
import com.example.ms_auditoria.model.Auditoria;
import com.example.ms_auditoria.repository.AuditoriaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditoriaService {

    private final AuditoriaRepository auditoriaRepository;
    private final WebClient webClient;

    private void validarUsuario(Long usuarioId) {
        try {
            log.info("Validando existencia de usuario ID: {} en ms-usuario", usuarioId);
            webClient.get()
                    .uri("/usuarios/{id}", usuarioId)
                    .retrieve()
                    .onStatus(status -> status.isError(), response -> {
                        log.error("Usuario {} no existe en ms-usuario", usuarioId);
                        return Mono.error(new RuntimeException("Usuario no existe en ms-usuario"));
                    })
                    .bodyToMono(UsuarioResponseDTO.class)
                    .block();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error de conexión con ms-usuario: {}", e.getMessage());
            throw new RuntimeException("No se puede validar el usuario en ms-usuario: " + e.getMessage());
        }
    }

    private AuditoriaResponseDTO mapToDTO(Auditoria auditoria) {
        return new AuditoriaResponseDTO(
                auditoria.getId(),
                auditoria.getUsuarioId(),
                auditoria.getAccion(),
                auditoria.getTablaAfectada(),
                auditoria.getRegistroId(),
                auditoria.getFecha(),
                auditoria.getDetalle());
    }

    public List<AuditoriaResponseDTO> listar() {
        return auditoriaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AuditoriaResponseDTO> obtenerPorId(Long id) {
        return auditoriaRepository.findById(id).map(this::mapToDTO);
    }

    @Transactional
    public AuditoriaResponseDTO crear(AuditoriaRequestDTO dto) {
        validarUsuario(dto.getUsuarioId());

        Auditoria auditoria = new Auditoria(null,
                dto.getUsuarioId(),
                dto.getAccion(),
                dto.getTablaAfectada(),
                dto.getRegistroId(),
                dto.getFecha(),
                dto.getDetalle());

        return mapToDTO(auditoriaRepository.save(auditoria));
    }

    @Transactional
    public Optional<AuditoriaResponseDTO> actualizar(Long id, AuditoriaRequestDTO dto) {
        return auditoriaRepository.findById(id).map(auditoria -> {
            validarUsuario(dto.getUsuarioId());
            auditoria.setUsuarioId(dto.getUsuarioId());
            auditoria.setAccion(dto.getAccion());
            auditoria.setTablaAfectada(dto.getTablaAfectada());
            auditoria.setRegistroId(dto.getRegistroId());
            auditoria.setFecha(dto.getFecha());
            auditoria.setDetalle(dto.getDetalle());
            return mapToDTO(auditoriaRepository.save(auditoria));
        });
    }

    @Transactional
    public void eliminar(Long id) {
        auditoriaRepository.deleteById(id);
    }
}
