FROM eclipse-temurin:21-jdk

# Instalar librerías necesarias para JasperReports
RUN apt-get update && apt-get install -y \
    libfreetype6 \
    fontconfig \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY . .

# Dar permisos al wrapper y compilar
RUN chmod +x ./mvnw
RUN ./mvnw package -DskipTests

CMD ["java", "-jar", "target/demo-0.0.1-SNAPSHOT.jar"]
