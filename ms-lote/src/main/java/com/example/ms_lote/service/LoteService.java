package com.example.ms_lote.service;

import com.example.ms_lote.dto.LoteRequestDTO;
import com.example.ms_lote.dto.LoteResponseDTO;
import com.example.ms_lote.dto.ProductoResponseDTO;
import com.example.ms_lote.model.Lote;
import com.example.ms_lote.repository.LoteRepository;
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
public class LoteService {

    private final LoteRepository loteRepository;
    private final WebClient webClient;

    private void validarProducto(Long productoId) {
        try {
            log.info("Validando producto ID {} en ms-producto", productoId);
            webClient.get()
                    .uri("/productos/{id}", productoId)
                    .retrieve()
                    .onStatus(status -> status.isError(), response -> {
                        log.error("Producto {} no existe en ms-producto", productoId);
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

    private LoteResponseDTO mapToDTO(Lote lote) {
        return new LoteResponseDTO(
                lote.getId(),
                lote.getProductoId(),
                lote.getCodigoLote(),
                lote.getCantidadInicial(),
                lote.getCantidadActual(),
                lote.getFechaIngreso(),
                lote.getFechaVencimiento());
    }

    public List<LoteResponseDTO> listar() {
        return loteRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<LoteResponseDTO> obtenerPorId(Long id) {
        return loteRepository.findById(id).map(this::mapToDTO);
    }

    @Transactional
    public LoteResponseDTO crear(LoteRequestDTO dto) {
        validarProducto(dto.getProductoId());

        Lote lote = new Lote(null,
                dto.getProductoId(),
                dto.getCodigoLote(),
                dto.getCantidadInicial(),
                dto.getCantidadActual(),
                LocalDateTime.now(),
                dto.getFechaVencimiento());

        return mapToDTO(loteRepository.save(lote));
    }

    @Transactional
    public Optional<LoteResponseDTO> actualizar(Long id, LoteRequestDTO dto) {
        return loteRepository.findById(id).map(lote -> {
            validarProducto(dto.getProductoId());
            lote.setProductoId(dto.getProductoId());
            lote.setCodigoLote(dto.getCodigoLote());
            lote.setCantidadInicial(dto.getCantidadInicial());
            lote.setCantidadActual(dto.getCantidadActual());
            lote.setFechaVencimiento(dto.getFechaVencimiento());
            return mapToDTO(loteRepository.save(lote));
        });
    }

    @Transactional
    public void eliminar(Long id) {
        loteRepository.deleteById(id);
    }
}
