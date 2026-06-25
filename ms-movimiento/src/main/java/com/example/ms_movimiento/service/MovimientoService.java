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

        log.info("Registrando movimiento: {} para producto {}", 
                request.getTipo(), request.getProductoId());

        // 1. VALIDAR PRODUCTO EN MS-PRODUCTO
        webClient.get()
                .uri(productoServiceUrl + "/productos/{id}", request.getProductoId())
                .retrieve()
                .onStatus(status -> status.isError(),
                        response -> Mono.error(new RuntimeException("Producto no existe")))
                .bodyToMono(String.class)
                .block();

        // 2. CALCULAR AJUSTE DE STOCK
        int cantidad = request.getCantidad();

        int ajuste = "SALIDA".equalsIgnoreCase(request.getTipo())
                ? -cantidad
                : cantidad;

        // 3. ACTUALIZAR INVENTARIO EN MS-INVENTARIO
        InventarioUpdateDTO dto = new InventarioUpdateDTO(
                request.getProductoId(),
                ajuste
        );

        webClient.post()
                .uri(inventarioServiceUrl + "/inventario/actualizar")
                .bodyValue(dto)
                .retrieve()
                .onStatus(status -> status.isError(),
                        response -> Mono.error(new RuntimeException("Error al actualizar inventario")))
                .bodyToMono(Void.class)
                .block();

        // 4. GUARDAR MOVIMIENTO EN BD LOCAL
        Movimiento movimiento = Movimiento.builder()
                .productoId(request.getProductoId())
                .cantidad(request.getCantidad())
                .tipo(request.getTipo().toUpperCase())
                .build();

        movimientoRepository.save(movimiento);
    }

    // ===================== LISTAR MOVIMIENTOS =====================

    @Transactional(readOnly = true)
    public List<MovimientoResponseDTO> listarTodo() {
        return movimientoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    private MovimientoResponseDTO mapToDTO(Movimiento m) {
        return MovimientoResponseDTO.builder()
                .id(m.getId())
                .productoId(m.getProductoId())
                .cantidad(m.getCantidad())
                .tipo(m.getTipo())
                .fecha(m.getFecha())
                .build();
    }
}