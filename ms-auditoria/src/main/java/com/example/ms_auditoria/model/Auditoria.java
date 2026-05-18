package com.example.ms_auditoria.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "auditorias")
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;

    @Column(nullable = false, length = 100)
    private String accion;

    @Column(name = "tabla_afectada", nullable = false, length = 100)
    private String tablaAfectada;

    @Column(name = "registro_id", nullable = false)
    private Long registroId;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 500)
    private String detalle;
}
