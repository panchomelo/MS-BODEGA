package com.example.ms_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient // IE 3.3.2: Permite que el Gateway sea descubierto por otros servicios y registre sus propias rutas dinámicas [1], [4]
public class MsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsGatewayApplication.class, args);
    }

}