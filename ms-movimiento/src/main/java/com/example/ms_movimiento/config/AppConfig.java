package com.example.ms_movimiento.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    /**
     * Define el Bean de WebClient para la comunicación entre microservicios.
     * Este componente será inyectado en MovimientoService para validar 
     * productos y actualizar el stock (IE 2.4.1).
     */
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}