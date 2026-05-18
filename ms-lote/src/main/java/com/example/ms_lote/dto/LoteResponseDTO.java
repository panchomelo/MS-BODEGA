package com.example.ms_lote.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoteResponseDTO {

    private Long id;
    private Long productoId;
    private String codigoLote;
    private Integer cantidadInicial;
    private Integer cantidadActual;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaVencimiento;
}
