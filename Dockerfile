# Imagen base con Java 21
FROM eclipse-temurin:21-jdk

# Instalar librerías necesarias para JasperReports
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

# Crear directorio de trabajo
WORKDIR /app

# Copiar todo tu proyecto al contenedor
COPY . .

# Compilar con Maven (usa el wrapper si lo tienes)
RUN ./mvnw package -DskipTests

# Ejecutar la aplicación Spring Boot
CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
