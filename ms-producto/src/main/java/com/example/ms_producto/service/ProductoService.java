package com.example.ms_producto.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.ms_producto.dto.ProductoRequestDTO;
import com.example.ms_producto.dto.ProductoResponseDTO;
import com.example.ms_producto.model.Producto;
import com.example.ms_producto.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductoService {

    private final ProductoRepository repository;
    private final WebClient webClient;

    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto){
        log.info("Iniciando la creación del producto: {}", dto.getNombre());
        
        validarCategoria(dto.getCategoriaId());

        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .categoriaId(dto.getCategoriaId())
                .build();

        Producto guardado = repository.save(producto);
        log.info("Producto guardado exitosamente con ID: {}", guardado.getId());
        
        return mapToResponse(guardado);
    }

    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar(){
        log.info("Recuperando lista completa de productos");
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id){
        log.info("Buscando producto con ID: {}", id);
        return mapToResponse(buscarProducto(id));
    }

    @Transactional
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto){
        log.info("Actualizando datos del producto ID: {}", id);
        Producto producto = buscarProducto(id);

        validarCategoria(dto.getCategoriaId());

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoriaId(dto.getCategoriaId());

        Producto actualizado = repository.save(producto);
        log.info("Producto ID: {} actualizado correctamente", id);
        
        return mapToResponse(actualizado);
    }

    @Transactional
    public void eliminar(Long id){
        log.warn("Eliminando definitivamente el producto ID: {}", id);
        Producto producto = buscarProducto(id);
        repository.delete(producto);
        log.info("Producto ID: {} eliminado de la base de datos", id);
    }

    // 4. Método privado para encapsular la llamada al microservicio de categorías
    private void validarCategoria(Long categoriaId) {
        log.info("Validando existencia de categoría ID: {} en ms-categoria", categoriaId);
        webClient.get()
                .uri("/{id}", categoriaId)
                .retrieve()
                .onStatus(status -> status.isError(), response -> {
                log.error("Error: La categoría {} no existe en el sistema", categoriaId);
                return Mono.error(new RuntimeException("Categoría no encontrada en el maestro de categorías"));
})
                .bodyToMono(Object.class)
                .block();
    }

    private Producto buscarProducto(Long id){
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Error: Producto con ID {} no encontrado", id);
                    return new RuntimeException("Producto no encontrado");
                });
    }

    private ProductoResponseDTO mapToResponse(Producto p){
        return ProductoResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .precio(p.getPrecio())
                .categoriaId(p.getCategoriaId())
                .build();
    }
}