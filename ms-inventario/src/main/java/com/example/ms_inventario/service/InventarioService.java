package com.example.ms_inventario.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // IE 2.2.3: Asegura integridad
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.example.ms_inventario.dto.InventarioRequestDTO;
import com.example.ms_inventario.dto.InventarioResponseDTO; // Requisito para errores reactivos [2]
import com.example.ms_inventario.dto.ProductoDTO;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository; // Molde para ms-producto [3]

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {

    private final InventarioRepository inventarioRepository;
    private final WebClient webClient; // Inyectado desde AppConfig [5]

    private void validarProducto(Long productoId) {
        try {
            webClient.get()
                    .uri("/productos/{id}", productoId)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .block();
        } catch (WebClientResponseException.NotFound e) {
            log.error("Error: Producto ID {} no existe en el sistema maestro", productoId);
            throw new RuntimeException("Producto no encontrado");
        } catch (Exception e) {
            throw new RuntimeException("No se puede conectar con ms-producto: " + e.getMessage());
        }
    }

    /**
     * Actualiza el stock de un producto validando su existencia remota.
     * Si la cantidad es negativa, se interpreta como una salida (ej. venta).
     */
    @Transactional
    public void actualizarStock(Long productoId, Integer cantidad) {
        log.info("Iniciando actualización de stock para producto ID: {} (Cantidad: {})", productoId, cantidad);

        // 1. Comunicación Remota (IE 2.4.1): Validar producto en ms-producto
        validarProducto(productoId);

        // 2. Regla simple: no permitir crear duplicados por productoId
        Optional<Inventario> existente = inventarioRepository.findByProductoId(productoId);
        if (existente.isPresent()) {
            log.warn("Producto ID {} ya tiene inventario registrado", productoId);
            throw new RuntimeException("productoId ya existe");
        }

        Inventario inventario = Inventario.builder()
                .productoId(productoId)
                .stock(0)
                .build();

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

    @Transactional
    public InventarioResponseDTO guardar(InventarioRequestDTO dto) {
        // 1. Comunicación Remota (IE 2.4.1): Validar producto en ms-producto
        validarProducto(dto.getProductoId());

        // 2. Regla simple: no permitir crear duplicados por productoId
        Optional<Inventario> existente = inventarioRepository.findByProductoId(dto.getProductoId());
        if (existente.isPresent()) {
            log.warn("Producto ID {} ya tiene inventario registrado", dto.getProductoId());
            throw new RuntimeException("productoId ya existe");
        }

        Inventario inventario = Inventario.builder()
                .productoId(dto.getProductoId())
                .stock(0)
                .build();

        // 3. Regla de Negocio (IE 2.2.1): Validar stock insuficiente
        int nuevoStock = inventario.getStock() + dto.getStock();
        if (nuevoStock < 0) {
            log.warn("Intento de rebaja fallido: Stock insuficiente para producto ID {}", dto.getProductoId());
            throw new RuntimeException("Stock insuficiente para realizar el descuento");
        }

        inventario.setStock(nuevoStock);
        return mapToResponse(inventarioRepository.save(inventario));
    }

    @Transactional
    public Optional<InventarioResponseDTO> actualizar(Long id, InventarioRequestDTO dto) {
        return inventarioRepository.findById(id).map(existente -> {
            // 1. Comunicación Remota (IE 2.4.1): Validar producto en ms-producto
            validarProducto(dto.getProductoId());

            // 2. Regla simple: no permitir cambio de productoId
            if (!dto.getProductoId().equals(existente.getProductoId())) {
                throw new RuntimeException("productoId no coincide con el inventario existente");
            }

            // 3. Regla de Negocio (IE 2.2.1): Validar stock insuficiente
            int nuevoStock = existente.getStock() + dto.getStock();
            if (nuevoStock < 0) {
                log.warn("Intento de rebaja fallido: Stock insuficiente para producto ID {}", dto.getProductoId());
                throw new RuntimeException("Stock insuficiente para realizar el descuento");
            }

            existente.setStock(nuevoStock);
            return mapToResponse(inventarioRepository.save(existente));
        });
    }

    private InventarioResponseDTO mapToResponse(Inventario i) {
        return InventarioResponseDTO.builder()
                .id(i.getId())
                .productoId(i.getProductoId())
                .stock(i.getStock())
                .build();
    }
}