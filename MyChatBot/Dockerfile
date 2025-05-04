# Use an official Java runtime
FROM eclipse-temurin:24-jdk

# Set working directory
WORKDIR /app

# Set words json
COPY src/main/java/org/data/allWordsSorted.json allWordsSorted.json

# Copy the JAR file into the container
COPY target/MyChatBot-1.0-SNAPSHOT.jar app.jar

# Run the JAR file
CMD ["java", "-jar", "app.jar"]
