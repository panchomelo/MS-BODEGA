package com.example.ms_proveedor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_proveedor.model.Proveedor;

public interface ProveedorRepository extends JpaRepository<Proveedor, Long> {
}
