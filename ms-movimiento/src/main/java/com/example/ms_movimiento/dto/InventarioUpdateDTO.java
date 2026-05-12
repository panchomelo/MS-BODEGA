package com.example.ms_movimiento.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventarioUpdateDTO {
    private Long productoId;
    private Integer cantidad;
}