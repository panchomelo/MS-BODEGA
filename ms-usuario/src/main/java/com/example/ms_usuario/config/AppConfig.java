package com.example.ms_usuario.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * Clase de configuración para el microservicio de usuarios.
 * Define los componentes necesarios para la comunicación entre servicios.
 */
@Configuration
public class AppConfig {

    /**
     * Define el Bean de WebClient para la comunicación remota.
     * Mantiene la consistencia con la arquitectura del sistema de bodega [1-4].
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}