package com.example.ms_proveedor.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_proveedor.dto.ProveedorRequestDTO;
import com.example.ms_proveedor.dto.ProveedorResponseDTO;
import com.example.ms_proveedor.model.Proveedor;
import com.example.ms_proveedor.repository.ProveedorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProveedorService {

    private final ProveedorRepository repository;
    private final WebClient webClient;

    @Transactional
    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {
        log.info("Iniciando creación de proveedor: {}", dto.getNombre());
        validarProducto(dto.getProductoId());

        Proveedor proveedor = Proveedor.builder()
                .nombre(dto.getNombre())
                .productoId(dto.getProductoId())
                .email(dto.getEmail())
                .telefono(dto.getTelefono())
                .direccion(dto.getDireccion())
                .build();

        Proveedor guardado = repository.save(proveedor);
        log.info("Proveedor guardado exitosamente con ID: {}", guardado.getId());

        return mapToResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProveedorResponseDTO> listar() {
        log.info("Recuperando lista de proveedores");
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProveedorResponseDTO obtenerPorId(Long id) {
        log.info("Buscando proveedor con ID: {}", id);
        return mapToResponse(buscarProveedor(id));
    }

    @Transactional
    public ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto) {
        log.info("Actualizando proveedor ID: {}", id);
        Proveedor proveedor = buscarProveedor(id);

        validarProducto(dto.getProductoId());

        proveedor.setNombre(dto.getNombre());
        proveedor.setProductoId(dto.getProductoId());
        proveedor.setEmail(dto.getEmail());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setDireccion(dto.getDireccion());

        Proveedor actualizado = repository.save(proveedor);
        log.info("Proveedor ID: {} actualizado correctamente", id);

        return mapToResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        log.warn("Eliminando proveedor ID: {}", id);
        Proveedor proveedor = buscarProveedor(id);
        repository.delete(proveedor);
        log.info("Proveedor ID: {} eliminado de la base de datos", id);
    }

    private void validarProducto(Long productoId) {
        log.info("Validando existencia de producto ID: {} en ms-producto", productoId);
        webClient.get()
                .uri("/{id}", productoId)
                .retrieve()
                .onStatus(status -> status.isError(), response -> {
                    log.error("Error: El producto {} no existe en ms-producto", productoId);
                    return Mono.error(new RuntimeException("Producto no encontrado en ms-producto"));
                })
                .bodyToMono(Object.class)
                .block();
    }

    private Proveedor buscarProveedor(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error: Proveedor con ID {} no encontrado", id);
                    return new RuntimeException("Proveedor no encontrado");
                });
    }

    private ProveedorResponseDTO mapToResponse(Proveedor p) {
        return ProveedorResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .productoId(p.getProductoId())
                .email(p.getEmail())
                .telefono(p.getTelefono())
                .direccion(p.getDireccion())
                .build();
    }
}
