# ===============================
# 🔹 ETAPA 1 — BUILD (Gradle Wrapper)
# ===============================
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Copiar solo los archivos necesarios para resolver dependencias primero
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Permite correr gradlew
RUN chmod +x gradlew

# Descargar dependencias sin copiar el código completo (cachea mejor)
RUN ./gradlew dependencies --no-daemon || true

# Ahora sí copiamos el código fuente
COPY src src

# Compilación — genera el JAR
RUN ./gradlew clean bootJar --no-daemon


# ===============================
# 🔹 ETAPA 2 — RUNTIME (imagen liviana)
# ===============================
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Puerto expuesto por Spring Boot
EXPOSE 8080

# Copiar SOLO el JAR desde la etapa de build
COPY --from=build /app/build/libs/*.jar app.jar

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
