package com.example.ms_proveedor.config;

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
                        .title("MS Proveedor - Gestión de proveedores")
                        .version("1.0")
                        .description("API de proveedores para la bodega: consulta, registro, actualización y eliminación de proveedores, con integración al ecosistema ms-bodega."));
    }
}
