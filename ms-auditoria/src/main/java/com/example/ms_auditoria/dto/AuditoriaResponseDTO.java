package com.example.ms_auditoria.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditoriaResponseDTO {

    private Long id;
    private Long usuarioId;
    private String accion;
    private String tablaAfectada;
    private Long registroId;
    private LocalDateTime fecha;
    private String detalle;
}
