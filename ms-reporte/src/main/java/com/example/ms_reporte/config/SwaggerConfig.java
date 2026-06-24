package com.example.ms_reporte.config;

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
                        .title("MS Reporte - Generación de reportes")
                        .version("1.0")
                        .description("API de reportes para la bodega: consulta, creación, actualización y eliminación de reportes operativos y de gestión."));
    }
}
