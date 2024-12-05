# Use an official OpenJDK 21 slim-buster image as the base
FROM openjdk:21-slim-buster

# Set the working directory in the container
WORKDIR /Schdlr

# Copy the JAR file into the container
# Use a wildcard to match the JAR file, allowing for version changes
COPY target/Schdlr*.jar Schdlr.jar

# Expose the port that your Spring Boot app uses
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "Schdlr.jar"]
