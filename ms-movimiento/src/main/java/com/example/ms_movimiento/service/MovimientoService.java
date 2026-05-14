package com.example.ms_movimiento.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_movimiento.dto.InventarioUpdateDTO;
import com.example.ms_movimiento.dto.MovimientoRequestDTO;
import com.example.ms_movimiento.dto.MovimientoResponseDTO;
import com.example.ms_movimiento.model.Movimiento;
import com.example.ms_movimiento.repository.MovimientoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final WebClient webClient;

    /**
     * Registra un movimiento y orquesta la comunicación con otros microservicios.
     */
    @Transactional
    public void registrarMovimiento(MovimientoRequestDTO request) {
        log.info("Registrando movimiento de tipo {} para producto ID: {}", request.getTipo(), request.getProductoId());

        // 1. Validar producto en ms-producto (IE 2.4.1)
        webClient.get()
                .uri("http://localhost:8080/productos/{id}", request.getProductoId())
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                    Mono.error(new RuntimeException("Producto no existe")))
                .bodyToMono(Object.class)
                .block();

        // 2. Lógica para evitar "Unboxing possibly null value"
        Integer cantidadOriginal = request.getCantidad();
        int cantidadLimpia = (cantidadOriginal != null) ? cantidadOriginal : 0;

        int cantidadAjuste = request.getTipo().equalsIgnoreCase("SALIDA")
                            ? -cantidadLimpia : cantidadLimpia;

        // 3. Actualizar stock en ms-inventario (Orquestación)
        InventarioUpdateDTO updateStock = new InventarioUpdateDTO(request.getProductoId(), cantidadAjuste);

        webClient.post()
                .uri("http://localhost:8082/inventario/actualizar")
                .bodyValue(updateStock)
                .retrieve()
                .onStatus(status -> status.isError(), response -> 
                    Mono.error(new RuntimeException("Error al actualizar inventario o stock insuficiente")))
                .bodyToMono(Void.class)
                .block();

        // 4. Guardar el historial del movimiento
        Movimiento movimiento = Movimiento.builder()
                .productoId(request.getProductoId())
                .cantidad(request.getCantidad())
                .tipo(request.getTipo().toUpperCase())
                .build();
        
        movimientoRepository.save(movimiento);
        log.info("Movimiento guardado y stock actualizado correctamente");
    }

    /**
     * Método necesario para el Controller (IE 2.1.2)
     */
    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> listarTodo() {
        log.info("Consultando historial completo de movimientos");
        return movimientoRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Mapeador interno de Entidad a DTO
     */
    private MovimientoResponseDTO mapToResponse(Movimiento m) {
        return MovimientoResponseDTO.builder()
                .id(m.getId())
                .productoId(m.getProductoId())
                .cantidad(m.getCantidad())
                .tipo(m.getTipo())
                .fecha(m.getFecha())
                .build();
    }
}