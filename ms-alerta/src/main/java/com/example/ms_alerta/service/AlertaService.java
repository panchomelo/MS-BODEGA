package com.example.ms_alerta.service;

import com.example.ms_alerta.dto.AlertaRequestDTO;
import com.example.ms_alerta.dto.AlertaResponseDTO;
import com.example.ms_alerta.dto.ProductoResponseDTO;
import com.example.ms_alerta.model.Alerta;
import com.example.ms_alerta.repository.AlertaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertaService {

    private final AlertaRepository alertaRepository;
    private final WebClient webClient;

    private AlertaResponseDTO mapToDTO(Alerta alerta) {
        return new AlertaResponseDTO(
                alerta.getId(),
                alerta.getProductoId(),
                alerta.getFechaGeneracion(),
                alerta.getTipoAlerta(),
                alerta.getEstado(),
                alerta.getMensaje());
    }

    private void validarProducto(Long productoId) {
        try {
            log.info("Validando existencia del producto ID: {} en ms-producto", productoId);
            webClient.get()
                    .uri("/productos/{id}", productoId)
                    .retrieve()
                    .onStatus(status -> status.isError(), response -> {
                        log.error("Producto {} no encontrado en ms-producto", productoId);
                        return Mono.error(new RuntimeException("Producto no existe en ms-producto"));
                    })
                    .bodyToMono(ProductoResponseDTO.class)
                    .block();

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error de conexión con ms-producto: {}", e.getMessage());
            throw new RuntimeException("No se puede validar el producto en ms-producto: " + e.getMessage());
        }
    }

    public List<AlertaResponseDTO> listar() {
        return alertaRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AlertaResponseDTO> obtenerPorId(Long id) {
        return alertaRepository.findById(id).map(this::mapToDTO);
    }

    @Transactional
    public AlertaResponseDTO crear(AlertaRequestDTO dto) {
        validarProducto(dto.getProductoId());

        Alerta alerta = new Alerta(null,
                dto.getProductoId(),
                LocalDateTime.now(),
                dto.getTipoAlerta(),
                dto.getEstado(),
                dto.getMensaje());

        return mapToDTO(alertaRepository.save(alerta));
    }

    @Transactional
    public Optional<AlertaResponseDTO> actualizar(Long id, AlertaRequestDTO dto) {
        return alertaRepository.findById(id).map(alerta -> {
            validarProducto(dto.getProductoId());
            alerta.setProductoId(dto.getProductoId());
            alerta.setTipoAlerta(dto.getTipoAlerta());
            alerta.setEstado(dto.getEstado());
            alerta.setMensaje(dto.getMensaje());
            return mapToDTO(alertaRepository.save(alerta));
        });
    }

    @Transactional
    public void eliminar(Long id) {
        alertaRepository.deleteById(id);
    }
}
