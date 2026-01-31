FROM maven:3.9.3-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy pom.xml first to resolve dependencies (Docker cache optimization)
COPY pom.xml .

# Download dependencies
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Compile code
RUN mvn clean compile

# Run GoogleTest
CMD ["mvn", "exec:java", "-Dexec.mainClass=Test01.GoogleTest"]
