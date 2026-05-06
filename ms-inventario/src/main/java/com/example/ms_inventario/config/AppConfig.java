package com.example.ms_inventario.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${ms.producto.url}")
    private String productoUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(productoUrl)
                .build();
    }
}