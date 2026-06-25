package com.example.ms_movimiento.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${ms.producto.url:http://localhost:8080}")
    private String productoServiceUrl;

    @Value("${ms.inventario.url:http://localhost:8082}")
    private String inventarioServiceUrl;

    @Transactional
    public void registrarMovimiento(MovimientoRequestDTO request) {
        log.info("Iniciando orquestación de movimiento: {} para producto ID: {}", 
                 request.getTipo(), request.getProductoId());

        // 1. Validar producto
        webClient.get()
                .uri(productoServiceUrl + "/productos/{id}", request.getProductoId())
                .retrieve()
                .onStatus(status -> status.isError(), response ->
                    Mono.error(new RuntimeException("Error: El producto con ID " + request.getProductoId() + " no existe")))
                .bodyToMono(Object.class)
                .block();

        // 2. Lógica de cálculo
        int cantidadLimpia = (request.getCantidad() != null) ? request.getCantidad() : 0;
        int cantidadAjuste = request.getTipo().equalsIgnoreCase("SALIDA") ? -cantidadLimpia : cantidadLimpia;

        // 3. Actualizar stock (Usando asignación nativa para evitar líos de constructores)
        InventarioUpdateDTO updateStock = new InventarioUpdateDTO();
        updateStock.setProductoId(request.getProductoId());
        updateStock.setCantidad(cantidadAjuste);

        webClient.post()
                .uri(inventarioServiceUrl + "/inventario/actualizar")
                .bodyValue(updateStock)
                .retrieve()
                .onStatus(status -> status.isError(), response -> 
                    Mono.error(new RuntimeException("Error crítico: Falló la actualización de inventario o stock insuficiente")))
                .bodyToMono(Void.class)
                .block();

        // 4. Guardar historial (Usando el Builder manual de respaldo que creamos)
        Movimiento movimiento = Movimiento.builder()
                .productoId(request.getProductoId())
                .cantidad(request.getCantidad())
                .tipo(request.getTipo().toUpperCase())
                .build();
        
        movimientoRepository.save(movimiento);
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> listarTodo() {
        return movimientoRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private MovimientoResponseDTO mapToResponse(Movimiento m) {
        // Usando el Builder manual de respaldo que creamos
        return MovimientoResponseDTO.builder()
                .id(m.getId())
                .productoId(m.getProductoId())
                .cantidad(m.getCantidad())
                .tipo(m.getTipo())
                .fecha(m.getFecha())
                .build();
    }
}