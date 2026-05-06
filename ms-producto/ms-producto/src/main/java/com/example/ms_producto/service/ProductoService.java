package com.example.ms_producto.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.ms_producto.dto.ProductoRequestDTO;
import com.example.ms_producto.dto.ProductoResponseDTO;
import com.example.ms_producto.model.Producto;
import com.example.ms_producto.repository.ProductoRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductoService {

    private final ProductoRepository repository;

    public ProductoResponseDTO crear(ProductoRequestDTO dto){
        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .precio(dto.getPrecio())
                .categoriaId(dto.getCategoriaId())
                .build();

        return mapToResponse(repository.save(producto));
    }

    public List<ProductoResponseDTO> listar(){
        return repository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ProductoResponseDTO obtenerPorId(Long id){
        return mapToResponse(buscarProducto(id));
    }

    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto){
        Producto producto = buscarProducto(id);

        producto.setNombre(dto.getNombre());
        producto.setPrecio(dto.getPrecio());
        producto.setCategoriaId(dto.getCategoriaId());

        return mapToResponse(repository.save(producto));
    }

    public void eliminar(Long id){
        Producto producto = buscarProducto(id);
        repository.delete(producto);
    }

    private Producto buscarProducto(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
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