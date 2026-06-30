# Paso 1: Compilación (Revisa que tenga el AS build)
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Paso 2: Ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app

# Copia el JAR compilado desde la etapa "build"
COPY --from=build /app/target/*.jar /app/app.jar

# Copia la carpeta wallet desde la etapa "build"
COPY --from=build /app/src/main/resources/wallet /app/wallet

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]