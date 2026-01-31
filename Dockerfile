FROM maven:3.9.3-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy pom.xml first (better Docker caching)
COPY pom.xml .

# Download project dependencies
RUN mvn dependency:go-offline -B

# Copy your source code
COPY src ./src

# Build the project
RUN mvn clean package -DskipTests

# Run your main Selenium test
CMD ["mvn", "exec:java", "-Dexec.mainClass=Test901.Google"]
