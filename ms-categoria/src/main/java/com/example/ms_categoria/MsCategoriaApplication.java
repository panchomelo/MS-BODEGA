package com.example.ms_categoria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // Importante para IE 3.3.2 [1]

@SpringBootApplication
@EnableDiscoveryClient // Esta anotación activa el registro automático en Eureka [1]
public class MsCategoriaApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsCategoriaApplication.class, args);
    }
}