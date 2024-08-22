# Use an official openjdk runtime as a parent image
FROM openjdk:11-jre-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the built Kafka Streams application JAR into the container
COPY target/weather-alert-stream-app-1.0-SNAPSHOT.jar /app/weather-alert-stream-app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "weather-alert-stream-app.jar"]
#CMD ["/bin/sh"]
