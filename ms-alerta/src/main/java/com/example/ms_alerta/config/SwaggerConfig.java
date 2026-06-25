package com.example.ms_alerta.config;

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
                        .title("MS Alerta - Gestión de alertas")
                        .version("1.0")
                        .description(
                                "API de alertas para la bodega: consulta, creación, actualización y eliminación de notificaciones de inventario."));
    }
}
