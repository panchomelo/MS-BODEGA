package com.example.ms_producto.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_producto.model.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}