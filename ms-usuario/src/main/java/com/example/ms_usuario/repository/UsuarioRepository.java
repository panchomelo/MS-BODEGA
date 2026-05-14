package com.example.ms_usuario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_usuario.model.Usuario;
import java.util.Optional;

/**
 * Repositorio para la entidad Usuario.
 * Extiende JpaRepository para obtener operaciones CRUD automáticas (IE 2.1.2).
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Permite buscar un usuario por su nombre de usuario.
     * Es esencial para validar la integridad de los datos y evitar duplicados.
     */
    Optional<Usuario> findByUsername(String username);
}