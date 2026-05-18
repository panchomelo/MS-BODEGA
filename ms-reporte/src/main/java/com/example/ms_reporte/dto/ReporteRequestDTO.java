package com.example.ms_reporte.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteRequestDTO {

    @NotBlank(message = "El tipo de reporte es obligatorio")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String tipoReporte;

    @NotNull(message = "La fecha de generación es obligatoria")
    private LocalDateTime fechaGeneracion;

    @NotNull(message = "El id de usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "La URL de descarga es obligatoria")
    @Size(max = 255, message = "Máximo 255 caracteres")
    private String urlDescarga;
}
