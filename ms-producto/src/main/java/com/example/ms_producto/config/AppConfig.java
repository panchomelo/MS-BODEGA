package com.example.ms_producto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${ms.categoria.url}")
    private String categoriaUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(categoriaUrl)
                .build();
    }
}