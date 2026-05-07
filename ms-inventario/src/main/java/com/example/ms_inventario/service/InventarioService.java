package com.example.ms_inventario.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importante para integridad
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono; // Para el manejo de errores en WebClient

import com.example.ms_inventario.dto.InventarioRequestDTO;
import com.example.ms_inventario.dto.InventarioResponseDTO;
import com.example.ms_inventario.dto.ProductoDTO;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventarioService {

    private final InventarioRepository repository;
    private final WebClient webClient;

    @Transactional // Asegura que la persistencia en Oracle sea atómica
    public InventarioResponseDTO crear(InventarioRequestDTO dto) {
        log.info("Iniciando validación remota para producto ID: {}", dto.getProductoId());

        ProductoDTO producto = webClient.get()
                .uri("/productos/" + dto.getProductoId())
                .retrieve()
                .onStatus(status -> status.is4xxClientError(), response -> {
                    log.error("Producto no encontrado en ms-producto: {}", dto.getProductoId());
                    return Mono.error(new RuntimeException("El producto no existe en el catálogo"));
                })
                .bodyToMono(ProductoDTO.class)
                .block();

        log.info("Producto '{}' validado. Procediendo a guardar stock.", producto.getNombre());

        // Guardar inventario
        Inventario inventario = Inventario.builder()
                .productoId(dto.getProductoId())
                .stock(dto.getStock())
                .build();

        Inventario saved = repository.save(inventario);
        log.info("Stock registrado exitosamente para el producto: {}", producto.getNombre());

        return mapToResponse(saved, producto.getNombre());
    }

    @Transactional(readOnly = true)
    public List<InventarioResponseDTO> listar() {
        log.info("Recuperando listado de inventario con datos remotos");
        return repository.findAll().stream()
                .map(i -> {
                    ProductoDTO producto = obtenerProductoRemoto(i.getProductoId());
                    return mapToResponse(i, producto != null ? producto.getNombre() : "Desconocido");
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public InventarioResponseDTO obtenerPorProducto(Long productoId) {
        log.info("Consultando inventario específico para producto ID: {}", productoId);
        
        Inventario inv = repository.findByProductoId(productoId)
                .orElseThrow(() -> {
                    log.warn("No se encontró registro de inventario para el producto: {}", productoId);
                    return new RuntimeException("Inventario no encontrado");
                });

        ProductoDTO producto = obtenerProductoRemoto(productoId);
        return mapToResponse(inv, producto != null ? producto.getNombre() : "Producto no encontrado en catálogo");
    }

    // Método de apoyo para reutilizar la lógica de WebClient 
    private ProductoDTO obtenerProductoRemoto(Long id) {
        try {
            return webClient.get()
                    .uri("/productos/" + id)
                    .retrieve()
                    .bodyToMono(ProductoDTO.class)
                    .block();
        } catch (Exception e) {
            log.error("Fallo en la comunicación remota con ms-producto para ID: {}", id);
            return null;
        }
    }

    private InventarioResponseDTO mapToResponse(Inventario i, String nombreProducto) {
        return InventarioResponseDTO.builder()
                .id(i.getId())
                .productoId(i.getProductoId())
                .stock(i.getStock())
                .nombreProducto(nombreProducto)
                .build();
    }
}