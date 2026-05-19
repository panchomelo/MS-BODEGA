package com.example.ms_inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // IE 2.2.3: Asegura integridad
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_inventario.dto.InventarioResponseDTO; // Requisito para errores reactivos [2]
import com.example.ms_inventario.dto.ProductoDTO;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository; // Molde para ms-producto [3]

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final WebClient webClient; // Inyectado desde AppConfig [5]

    /**
     * Actualiza el stock de un producto validando su existencia remota.
     * Si la cantidad es negativa, se interpreta como una salida (ej. venta).
     */
    @Transactional
    public void actualizarStock(Long productoId, Integer cantidad) {
        log.info("Iniciando actualización de stock para producto ID: {} (Cantidad: {})", productoId, cantidad);

        // 1. Comunicación Remota (IE 2.4.1): Validar producto en ms-producto
        webClient.get()
                .uri("/productos/{id}", productoId)
                .retrieve()
                .onStatus(status -> status.isError(), response -> {
                    log.error("Error: Producto ID {} no existe en el sistema maestro", productoId);
                    return Mono.error(new RuntimeException("Producto no encontrado para el movimiento"));
                })
                .bodyToMono(ProductoDTO.class) // Mapea la respuesta al DTO local [3]
                .block(); // Sincroniza la validación antes de persistir

        // 2. Lógica de Persistencia: Buscar o inicializar registro
        Inventario inventario = inventarioRepository.findByProductoId(productoId)
                .orElse(Inventario.builder()
                        .productoId(productoId)
                        .stock(0)
                        .build());

        // 3. Regla de Negocio (IE 2.2.1): Validar stock insuficiente
        int nuevoStock = inventario.getStock() + cantidad;
        if (nuevoStock < 0) {
            log.warn("Intento de rebaja fallido: Stock insuficiente para producto ID {}", productoId);
            throw new RuntimeException("Stock insuficiente para realizar el descuento");
        }

        inventario.setStock(nuevoStock);
        inventarioRepository.save(inventario);
        log.info("Actualización exitosa. Producto ID: {}, Nuevo Stock: {}", productoId, nuevoStock);
    }

    @Transactional(readOnly = true)
    public List<InventarioResponseDTO> listarTodo() {
        log.info("Consultando stock general de la bodega");
        return inventarioRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    private InventarioResponseDTO mapToResponse(Inventario i) {
        return InventarioResponseDTO.builder()
                .id(i.getId())
                .productoId(i.getProductoId())
                .stock(i.getStock())
                .build();
    }
}