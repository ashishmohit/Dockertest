FROM selenium/standalone-chromium:latest

USER root

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:resolve

COPY src ./src

CMD ["mvn", "test"]
