# Paso 1: Imagen base ligera de Java 17 (Recomendación profesional IE 3.3.1)
# Se utiliza Alpine por ser rápida, liviana y segura [2-4]
FROM eclipse-temurin:17-jdk-alpine

# Paso 2: Directorio de trabajo dentro del contenedor [3]
WORKDIR /app

# Paso 3: Copiar el archivo JAR generado por Maven al contenedor
# Requisito: Haber ejecutado previamente 'mvn clean package' [3]
COPY target/*.jar app.jar

# Paso 4: Exponer el puerto en el que corre el microservicio
# Coherente con el server.port: 8085 definido en tu YAML [3, 5]
EXPOSE 8085

# Paso 5: Comando para ejecutar la aplicación al iniciar el contenedor [3]
ENTRYPOINT ["java", "-jar", "app.jar"]