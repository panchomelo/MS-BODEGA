# Paso 1: Compilación automática en los servidores de Render
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
# Compila el JAR saltándose los tests para que el despliegue sea rápido
RUN mvn clean package -DskipTests

# Paso 2: Imagen base ligera de Java 17 para ejecución
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia el archivo JAR generado en la etapa anterior
COPY --from=build /app/target/*.jar /app/app.jar

# Puerto estándar que Render espera internamente (8080)
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]