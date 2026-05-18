package com.example.ms_lote.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteRequestDTO {

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;

    @NotBlank(message = "El código de lote es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String codigoLote;

    @NotNull(message = "La cantidad inicial es obligatoria")
    @Positive(message = "La cantidad inicial debe ser mayor a 0")
    private Integer cantidadInicial;

    @NotNull(message = "La cantidad actual es obligatoria")
    @PositiveOrZero(message = "La cantidad actual debe ser mayor o igual a 0")
    private Integer cantidadActual;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDateTime fechaVencimiento;
}
