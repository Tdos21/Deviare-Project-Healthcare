# Use a base image
FROM openjdk:17-jdk-slim

# Define the build argument
ARG JAR_FILE=target/*.jar

# Copy the application JAR file to the container
COPY ${JAR_FILE} Medical_Supplies_App.jar

# Define the entry point
ENTRYPOINT ["java", "-jar", "/Medical_Supplies_App.jar"]
