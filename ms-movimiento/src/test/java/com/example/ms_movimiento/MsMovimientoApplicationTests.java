package com.example.ms_movimiento;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
// Evita que el test de contexto intente levantar la BD de Oracle real si no es necesario
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
class MsMovimientoApplicationTests {

    @Test
    void contextLoads() {
        // Verifica la carga limpia del contexto de Spring
    }

}