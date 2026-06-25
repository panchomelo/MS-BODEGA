package com.example.ms_movimiento;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // Importante para IE 3.3.2

@SpringBootApplication
@EnableDiscoveryClient // Activa el registro automático en el servidor Eureka
public class MsMovimientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsMovimientoApplication.class, args);
	}

}