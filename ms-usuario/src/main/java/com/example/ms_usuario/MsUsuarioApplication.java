package com.example.ms_usuario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // Requisito para Eureka (IE 3.3.2) [2]
public class MsUsuarioApplication {
    public static void main(String[] args) {
        SpringApplication.run(MsUsuarioApplication.class, args);
    }
}