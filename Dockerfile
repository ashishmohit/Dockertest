# Use Maven + Java 17 base image
FROM maven:3.9.3-eclipse-temurin-17

# Set working directory inside container
WORKDIR /app

# Copy only pom.xml first to cache dependencies
COPY pom.xml .

# Download all project dependencies offline (faster builds)
RUN mvn dependency:go-offline -B

# Copy the source code
COPY src ./src

# Build the project (skip tests inside Docker build)
RUN mvn clean package -DskipTests

# Set the command to run your Java Selenium test
# Using environment variable SELENIUM_URL if available
CMD ["mvn", "exec:java", "-Dexec.mainClass=Test901.Google"]
