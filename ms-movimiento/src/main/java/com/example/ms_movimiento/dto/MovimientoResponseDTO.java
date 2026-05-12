package com.example.ms_movimiento.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoResponseDTO {
    private Long id;
    private Long productoId;
    private Integer cantidad;
    private String tipo;
    private LocalDateTime fecha;
}