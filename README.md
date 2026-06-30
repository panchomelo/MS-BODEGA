# MS-BODEGA

Sistema de gestión de bodega basado en arquitectura de microservicios con Spring Boot, Eureka y API Gateway.

## Descripción del dominio

MS-BODEGA centraliza procesos de logística y control de inventario para una bodega.
El dominio cubre la administración de productos, categorías, proveedores, inventarios, lotes, movimientos, usuarios, alertas, auditoría y reportes.

Objetivos principales:
- Mantener trazabilidad de entradas y salidas de stock.
- Registrar eventos de auditoría y alertas operativas.
- Exponer APIs desacopladas por contexto funcional.
- Unificar el acceso a servicios mediante Gateway.

## Integrantes

- Francisco Vega
- Bastian Olivares

## Arquitectura base

- Descubrimiento de servicios: ms-eureka
- Puerta de enlace: ms-gateway
- Microservicios de negocio: ms-usuario, ms-inventario, ms-categoria, ms-movimiento, ms-lote, ms-reporte, ms-auditoria, ms-proveedor, ms-producto, ms-alerta

## Listado de microservicios

| Microservicio | Nombre en Eureka | Puerto por defecto |
|---|---|---|
| ms-eureka | ms-eureka | 8761 |
| ms-gateway | ms-gateway | 8080 |
| ms-usuario | ms-usuario | 8081 |
| ms-inventario | ms-inventario | 8082 |
| ms-categoria | ms-categoria | 8083 |
| ms-movimiento | ms-movimiento | 8084 |
| ms-lote | ms-lote | 8085 |
| ms-reporte | ms-reporte | 8086 |
| ms-auditoria | ms-auditoria | 8087 |
| ms-proveedor | ms-proveedor | 8088 |
| ms-producto | ms-producto | 8089 |
| ms-alerta | ms-alerta | 8090 |

## Rutas principales del Gateway

Gateway base: http://localhost:8080

- /alertas/**
- /auditorias/**
- /categorias/**
- /inventarios/**
- /lotes/**
- /movimientos/**
- /productos/**
- /proveedores/**
- /reportes/**
- /usuarios/**

## Documentación Swagger

Nota: los siguientes enlaces asumen ejecución local en los puertos por defecto.

### Swagger UI

- ms-usuario: http://localhost:8081/swagger-ui/index.html
- ms-inventario: http://localhost:8082/doc/swagger-ui.html
- ms-categoria: http://localhost:8083/swagger-ui/index.html
- ms-movimiento: http://localhost:8084/swagger-ui/index.html
- ms-lote: http://localhost:8085/swagger-ui/index.html
- ms-reporte: http://localhost:8086/doc/swagger-ui.html
- ms-auditoria: http://localhost:8087/doc/swagger-ui.html
- ms-proveedor: http://localhost:8088/doc/swagger-ui.html
- ms-producto: http://localhost:8089/swagger-ui/index.html
- ms-alerta: http://localhost:8090/doc/swagger-ui.html
- ms-gateway: http://localhost:8080/swagger-ui/index.html

### OpenAPI JSON

- ms-usuario: http://localhost:8081/v3/api-docs
- ms-inventario: http://localhost:8082/v3/api-docs
- ms-categoria: http://localhost:8083/v3/api-docs
- ms-movimiento: http://localhost:8084/v3/api-docs
- ms-lote: http://localhost:8085/v3/api-docs
- ms-reporte: http://localhost:8086/v3/api-docs
- ms-auditoria: http://localhost:8087/v3/api-docs
- ms-proveedor: http://localhost:8088/v3/api-docs
- ms-producto: http://localhost:8089/v3/api-docs
- ms-alerta: http://localhost:8090/v3/api-docs
- ms-gateway: http://localhost:8080/v3/api-docs

## Instrucciones de ejecución

### Requisitos

- Java 21
- Maven Wrapper (incluido en el repositorio)
- Base de datos Oracle accesible
- Variables de entorno mínimas por microservicio de datos:
  - JDBC_URL
  - DB_USERNAME
  - DB_PASSWORD

### Orden recomendado de arranque

1. Levantar Eureka
	- Entrar a carpeta ms-eureka
	- Ejecutar: mvnw.cmd spring-boot:run

2. Levantar microservicios de negocio
	- En cada carpeta de microservicio ejecutar: mvnw.cmd spring-boot:run
	- Servicios sugeridos: ms-usuario, ms-inventario, ms-categoria, ms-movimiento, ms-lote, ms-reporte, ms-auditoria, ms-proveedor, ms-producto, ms-alerta

3. Levantar Gateway
	- Entrar a carpeta ms-gateway
	- Ejecutar: mvnw.cmd spring-boot:run

### Verificación rápida

- Eureka Dashboard: http://localhost:8761
- Gateway activo: http://localhost:8080
- Probar una ruta de ejemplo: http://localhost:8080/productos
