package com.example.ms_reporte.service;

import com.example.ms_reporte.dto.ReporteRequestDTO;
import com.example.ms_reporte.dto.ReporteResponseDTO;
import com.example.ms_reporte.dto.UsuarioResponseDTO;
import com.example.ms_reporte.model.Reporte;
import com.example.ms_reporte.repository.ReporteRepository;
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
public class ReporteService {

    private final ReporteRepository reporteRepository;
    private final WebClient webClient;

    private void validarUsuario(Long usuarioId) {
        try {
            log.info("Validando existencia de usuario ID: {} en ms-usuario", usuarioId);
            webClient.get()
                    .uri("/usuarios/{id}", usuarioId)
                    .retrieve()
                    .onStatus(status -> status.isError(), response -> {
                        log.error("Usuario {} no encontrado en ms-usuario", usuarioId);
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

    private ReporteResponseDTO mapToDTO(Reporte reporte) {
        return new ReporteResponseDTO(
                reporte.getId(),
                reporte.getTipoReporte(),
                reporte.getFechaGeneracion(),
                reporte.getUsuarioId(),
                reporte.getUrlDescarga());
    }

    public List<ReporteResponseDTO> listar() {
        return reporteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<ReporteResponseDTO> obtenerPorId(Long id) {
        return reporteRepository.findById(id).map(this::mapToDTO);
    }

    @Transactional
    public ReporteResponseDTO crear(ReporteRequestDTO dto) {
        validarUsuario(dto.getUsuarioId());

        Reporte reporte = new Reporte(null,
                dto.getTipoReporte(),
                dto.getFechaGeneracion(),
                dto.getUsuarioId(),
                dto.getUrlDescarga());

        return mapToDTO(reporteRepository.save(reporte));
    }

    @Transactional
    public Optional<ReporteResponseDTO> actualizar(Long id, ReporteRequestDTO dto) {
        return reporteRepository.findById(id).map(reporte -> {
            validarUsuario(dto.getUsuarioId());
            reporte.setTipoReporte(dto.getTipoReporte());
            reporte.setFechaGeneracion(dto.getFechaGeneracion());
            reporte.setUsuarioId(dto.getUsuarioId());
            reporte.setUrlDescarga(dto.getUrlDescarga());
            return mapToDTO(reporteRepository.save(reporte));
        });
    }

    @Transactional
    public void eliminar(Long id) {
        reporteRepository.deleteById(id);
    }
}
