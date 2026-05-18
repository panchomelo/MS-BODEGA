package com.example.ms_reporte.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reportes")
public class Reporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_reporte", nullable = false, length = 100)
    private String tipoReporte;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDateTime fechaGeneracion;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(name = "url_descarga", nullable = false, length = 255)
    private String urlDescarga;
}
