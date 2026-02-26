# Usamos una imagen de Maven con Java 17 para compilar
FROM maven:3.8.5-openjdk-17 AS build
COPY . .
RUN mvn clean package -DskipTests

# Usamos una imagen liviana de Java 17 para ejecutar
FROM openjdk:17-jdk-slim
COPY --from=build /target/gestiongastos-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
