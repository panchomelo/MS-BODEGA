package com.example.ms_alerta.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaResponseDTO {

    private Long id;
    private Long productoId;
    private LocalDateTime fechaGeneracion;
    private String tipoAlerta;
    private String estado;
    private String mensaje;
}
