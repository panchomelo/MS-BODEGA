package com.example.ms_auditoria.dto;

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
public class AuditoriaRequestDTO {

    @NotNull(message = "El id de usuario es obligatorio")
    private Long usuarioId;

    @NotBlank(message = "La acción es obligatoria")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String accion;

    @NotBlank(message = "La tabla afectada es obligatoria")
    @Size(max = 100, message = "Máximo 100 caracteres")
    private String tablaAfectada;

    @NotNull(message = "El id del registro es obligatorio")
    private Long registroId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;

    @NotBlank(message = "El detalle es obligatorio")
    @Size(max = 500, message = "Máximo 500 caracteres")
    private String detalle;
}
