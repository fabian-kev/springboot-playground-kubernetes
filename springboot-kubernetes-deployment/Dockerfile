# Use an official OpenJDK runtime as a base image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the Spring Boot JAR (assuming it’s built with Maven/Gradle)
COPY target/springboot-kubernetes-deployment-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]