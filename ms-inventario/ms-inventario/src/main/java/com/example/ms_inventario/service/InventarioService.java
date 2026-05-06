package com.example.ms_inventario.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_inventario.dto.InventarioRequestDTO;
import com.example.ms_inventario.dto.InventarioResponseDTO;
import com.example.ms_inventario.dto.ProductoDTO;
import com.example.ms_inventario.model.Inventario;
import com.example.ms_inventario.repository.InventarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InventarioService {

    private final InventarioRepository repository;
    private final WebClient webClient;

    public InventarioResponseDTO crear(InventarioRequestDTO dto) {

        // 🔥 llamar microservicio producto
        ProductoDTO producto = webClient.get()
                .uri("/productos/" + dto.getProductoId())
                .retrieve()
                .bodyToMono(ProductoDTO.class)
                .block();

        if (producto == null) {
            throw new RuntimeException("Producto no existe con id: " + dto.getProductoId());
        }

        // 💾 guardar inventario
        Inventario inventario = Inventario.builder()
                .productoId(dto.getProductoId())
                .stock(dto.getStock())
                .build();

        Inventario saved = repository.save(inventario);

        // 🔁 respuesta final
        return InventarioResponseDTO.builder()
                .id(saved.getId())
                .productoId(saved.getProductoId())
                .stock(saved.getStock())
                .nombreProducto(producto.getNombre())
                .build();
    }

    public List<InventarioResponseDTO> listar() {

        return repository.findAll().stream()
                .map(i -> {

                    ProductoDTO producto = webClient.get()
                            .uri("/productos/" + i.getProductoId())
                            .retrieve()
                            .bodyToMono(ProductoDTO.class)
                            .block();

                    return InventarioResponseDTO.builder()
                            .id(i.getId())
                            .productoId(i.getProductoId())
                            .stock(i.getStock())
                            .nombreProducto(producto != null ? producto.getNombre() : null)
                            .build();
                })
                .toList();
    }

    public InventarioResponseDTO obtenerPorProducto(Long productoId) {

        Inventario inv = repository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));

        ProductoDTO producto = webClient.get()
                .uri("/productos/" + productoId)
                .retrieve()
                .bodyToMono(ProductoDTO.class)
                .block();

        return InventarioResponseDTO.builder()
                .id(inv.getId())
                .productoId(inv.getProductoId())
                .stock(inv.getStock())
                .nombreProducto(producto != null ? producto.getNombre() : null)
                .build();
    }
}