package com.example.ms_usuario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "usuarios") // Sigue el estándar de nombres en plural como en 'categorias' [1] y 'movimientos' [4]
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder // Facilita la creación de objetos, igual que en tus otras entidades [1-4]
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String nombre;

    private String email;

    private String rol; // Ejemplo: "ADMIN", "BODEGUERO"
}