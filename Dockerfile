# Paso 1: Compilar usando una imagen de Maven actualizada
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Paso 2: Ejecutar usando Eclipse Temurin (es la versión que reemplazó a la vieja de OpenJDK)
FROM eclipse-temurin:17-jdk-jammy
COPY --from=build /target/gestiongastos-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
