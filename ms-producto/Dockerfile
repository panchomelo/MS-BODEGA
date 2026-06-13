# Paso 1: Imagen base ligera de Java 17 (Recomendación profesional IE 3.3.1)
FROM eclipse-temurin:17-jdk-alpine

# Paso 2: Directorio de trabajo dentro del contenedor
WORKDIR /app

# Paso 3: Copiar el archivo JAR generado por Maven al contenedor
# Asegúrate de haber ejecutado 'mvn clean package' antes
COPY target/*.jar app.jar

# Paso 4: Exponer el puerto en el que corre tu microservicio
EXPOSE 8080

# Paso 5: Comando para ejecutar la aplicación al iniciar el contenedor
ENTRYPOINT ["java", "-jar", "app.jar"]