FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only the necessary files for the build
COPY pom.xml .
COPY .config .config
COPY .mvn .mvn
COPY mvnw .
COPY src src


# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final lightweight image
FROM bellsoft/liberica-openjre-alpine:21
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/demo-0.0.1-SNAPSHOT.jar /app/demo-0.0.1-SNAPSHOT.jar

# Expose the port your application runs on
EXPOSE 8080

# Specify the command to run on container start
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]