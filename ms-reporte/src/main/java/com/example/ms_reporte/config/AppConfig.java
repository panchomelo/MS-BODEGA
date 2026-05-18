package com.example.ms_reporte.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${ms.usuario.url}")
    private String usuarioUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(usuarioUrl)
                .build();
    }
}
