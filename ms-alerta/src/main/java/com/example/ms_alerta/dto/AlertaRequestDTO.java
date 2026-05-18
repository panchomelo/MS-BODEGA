package com.example.ms_alerta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaRequestDTO {

    @NotNull(message = "El id del producto es obligatorio")
    private Long productoId;

    @NotBlank(message = "El tipo de alerta es obligatorio")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String tipoAlerta;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 20, message = "Máximo 20 caracteres")
    private String estado;

    @Size(max = 255, message = "Máximo 255 caracteres")
    private String mensaje;
}
