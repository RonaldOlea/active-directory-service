# Use OpenJDK 17 slim as the base image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the local project files (assuming your compiled JAR is in the current directory)
COPY target/*.jar /app/active-directory.jar

# Expose the port the application will run on (change if necessary)
EXPOSE 8082

# Command to run the Java application (adjust the file name as needed)
ENTRYPOINT ["java", "-jar", "/app/active-directory.jar"]
