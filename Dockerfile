# ============================================================
# Stage 1: Build con Maven + Java 17
# ============================================================
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copia primero el pom.xml para aprovechar cache de dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copia el código fuente y compila
COPY src ./src
RUN mvn clean package -DskipTests -B

# ============================================================
# Stage 2: Runtime liviano con solo Java 17 JRE
# ============================================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copia el JAR generado en el stage anterior
COPY --from=build /app/target/aeespm-poem-backend-0.0.1-SNAPSHOT.jar app.jar

# Puerto que expone Spring Boot
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
