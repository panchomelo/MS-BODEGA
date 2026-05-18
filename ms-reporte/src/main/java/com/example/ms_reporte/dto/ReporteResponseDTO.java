package com.example.ms_reporte.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteResponseDTO {

    private Long id;
    private String tipoReporte;
    private LocalDateTime fechaGeneracion;
    private Long usuarioId;
    private String urlDescarga;
}
