package com.example.ms_producto.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    private final String categoriaUrl;

    public AppConfig(@Value("${ms.categoria.url:lb://ms-categoria}") String categoriaUrl) {
        this.categoriaUrl = categoriaUrl;
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(categoriaUrl)
                .build();
    }
}