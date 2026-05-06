package com.example.ms_inventario.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponseDTO {

    private Long id;
    private Long productoId;
    private Integer stock;

    private String nombreProducto;
}