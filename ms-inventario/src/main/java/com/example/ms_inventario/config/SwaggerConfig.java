package com.example.ms_inventario.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("MS Inventario - Gestión de stock")
                        .version("1.0")
                        .description(
                                "API de inventario para la bodega: consulta de stock, registro y actualización de inventarios, con validación de producto contra ms-producto."));
    }
}
