# ---- Stage 1: Build the application ----
FROM maven:3.9.9-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies (to cache them)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Package the application
RUN mvn clean package -DskipTests

# ---- Stage 2: Run the application ----
FROM eclipse-temurin:21-jre

# Set a working directory
WORKDIR /app

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose the port (Render typically uses $PORT)
EXPOSE 8080

# Use environment variable PORT if provided (Render sets this automatically)
ENV PORT=8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]
