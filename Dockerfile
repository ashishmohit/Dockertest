FROM selenium/standalone-chromium:latest

USER root

# Install Maven + Java
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean

# Verify installation
RUN mvn -v

WORKDIR /app
COPY . /app

CMD ["mvn", "test"]
