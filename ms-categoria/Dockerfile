# Paso 1: Imagen base ligera de Java 17 (Recomendación profesional IE 3.3.1)
FROM eclipse-temurin:17-jdk-alpine

# Paso 2: Directorio de trabajo dentro del contenedor
WORKDIR /app

# Paso 3: Copiar el archivo JAR generado por Maven al contenedor
# Asegúrate de haber ejecutado 'mvn clean package' antes de construir la imagen
COPY target/*.jar app.jar

# Paso 4: Exponer el puerto en el que corre tu microservicio
# Para ms-producto es 8080, para ms-categoria usa 8083 según conversamos
EXPOSE 8083

# Paso 5: Comando para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]