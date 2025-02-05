FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: Criando imagem final
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY --from=builder /app/target/insurance-0.0.1-SNAPSHOT.jar app.jar

# Definir profile para ambiente (padrão: dev)
ARG PROFILE=dev
ENV SPRING_PROFILES_ACTIVE=$PROFILE

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
