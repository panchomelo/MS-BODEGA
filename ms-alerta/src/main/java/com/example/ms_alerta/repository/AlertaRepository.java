package com.example.ms_alerta.repository;

import com.example.ms_alerta.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaRepository extends JpaRepository<Alerta, Long> {
}
