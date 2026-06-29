# Paso 1: Imagen base ligera de Java 17 (Recomendación IE 3.3.1)
# Se prefiere Alpine por ser liviana, rápida y segura [6, 7]
FROM eclipse-temurin:17-jdk-alpine

# Paso 2: Directorio de trabajo dentro del contenedor [7]
WORKDIR /app

# Paso 3: Copiar el archivo JAR generado por Maven al contenedor [7]
# NOTA: Debes ejecutar 'mvn clean package' en tu máquina antes de construir la imagen
COPY target/*.jar app.jar

# Paso 4: Exponer el puerto del Gateway [7]
# Según tu configuración previa, el Gateway centraliza el tráfico en el 8080 [8]
EXPOSE 8080

# Paso 5: Comando para ejecutar la aplicación al iniciar el contenedor [7]
ENTRYPOINT ["java", "-jar", "app.jar"]