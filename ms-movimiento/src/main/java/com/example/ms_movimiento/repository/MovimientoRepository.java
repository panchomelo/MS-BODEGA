package com.example.ms_movimiento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_movimiento.model.Movimiento;

/**
 * Repositorio para la entidad Movimiento.
 * Extiende JpaRepository para obtener operaciones CRUD automáticas (IE 2.1.2).
 */
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    /**
     * Permite obtener el historial de movimientos de un producto específico.
     * Útil para auditoría y trazabilidad de stock.
     */
    List<Movimiento> findByProductoId(Long productoId);
}