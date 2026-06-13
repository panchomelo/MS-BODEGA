package com.example.ms_producto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // Importante para IE 3.3.2

@SpringBootApplication
@EnableDiscoveryClient // Esta anotación activa el registro automático en Eureka
public class MsProductoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsProductoApplication.class, args);
    }
}