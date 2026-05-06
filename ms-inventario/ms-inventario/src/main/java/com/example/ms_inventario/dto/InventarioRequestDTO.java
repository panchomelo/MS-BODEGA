package com.example.ms_inventario.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventarioRequestDTO {

    @NotNull
    private Long productoId;

    @NotNull
    @Min(0)
    private Integer stock;
}