package com.example.ms_lote;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test") // Recomendado para usar una configuración aislada
class MsLoteApplicationTests {

    @Test
    void contextLoads() {
        // Esta prueba verifica que todos los beans de Spring (Servicios, Repositorios, etc.)
        // se instancien correctamente y que la conexión inicial a la BD (Oracle) sea válida [1].
    }

}