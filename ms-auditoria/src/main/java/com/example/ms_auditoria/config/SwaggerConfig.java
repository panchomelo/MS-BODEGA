package com.example.ms_auditoria.config;

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
                        .title("MS Auditoría - Seguimiento de acciones")
                        .version("1.0")
                        .description(
                                "API de auditoría para la bodega: registro, consulta y administración de eventos de usuario y cambios de sistema."));
    }
}
