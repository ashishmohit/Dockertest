FROM maven:3.9.3-eclipse-temurin-17

# Set working directory
WORKDIR /app

# Copy pom.xml for dependency caching
COPY pom.xml ./

# Download project dependencies offline
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build project
RUN mvn clean package -DskipTests

# Run Selenium Java test
CMD ["mvn", "exec:java", "-Dexec.mainClass=Test901.Google"]
