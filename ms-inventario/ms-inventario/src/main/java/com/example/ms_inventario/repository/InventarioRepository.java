package com.example.ms_inventario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_inventario.model.Inventario;

import java.util.Optional;

public interface InventarioRepository extends JpaRepository<Inventario, Long> {

    Optional<Inventario> findByProductoId(Long productoId);
}