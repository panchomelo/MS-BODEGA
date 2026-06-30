package com.example.ms_producto.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ms_producto.dto.ProductoRequestDTO;
import com.example.ms_producto.dto.ProductoResponseDTO;
import com.example.ms_producto.service.ProductoService;

import io.swagger.v3.oas.annotations.Operation; // Importante para IE 3.2.1
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/productos")
// @Tag agrupa los endpoints en la interfaz de Swagger [1], [2]
@Tag(name = "Gestión de Productos", description = "Endpoints para realizar operaciones CRUD sobre los productos de la bodega")
public class ProductoController {

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo producto", description = "Registra un producto en Oracle Cloud previa validación remota de categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error en la validación de los datos de entrada o categoría inexistente")
    })
    public ResponseEntity<ProductoResponseDTO> crear(@Valid @RequestBody ProductoRequestDTO dto) {
        log.info("Recibida solicitud para crear el producto: {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(dto));
    }

    @GetMapping
    @Operation(summary = "Listar todos los productos", description = "Obtiene la lista completa de productos registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida correctamente")
    public ResponseEntity<List<ProductoResponseDTO>> listar() {
        log.info("Solicitando lista completa de productos");
        return ResponseEntity.ok(service.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID", description = "Busca un producto específico utilizando su identificador único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no localizado en la base de datos")
    })
    public ResponseEntity<ProductoResponseDTO> obtener(@PathVariable Long id) {
        log.info("Buscando producto con ID: {}", id);
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto existente", description = "Modifica los datos de un producto basándose en su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado con éxito"),
        @ApiResponse(responseCode = "404", description = "No se pudo actualizar: ID no encontrado")
    })
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO dto) {
        
        log.info("Solicitud para actualizar producto ID: {}", id);
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un producto", description = "Remueve permanentemente un producto del registro de la bodega")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontró el producto para eliminar")
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("Eliminando producto ID: {}", id);
        service.eliminar(id);
        
        return ResponseEntity.noContent().build();
    }
}