package com.example.ms_inventario.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioRequestDTO {

    @NotNull(message = "El ID del producto es obligatorio para el registro")
    private Long productoId;

    @NotNull(message = "La cantidad de stock no puede ser nula")
    @Min(value = 0, message = "El stock no puede ser un valor negativo")
    private Integer stock;
    }