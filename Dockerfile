FROM selenium/standalone-chromium:latest

USER root

# Install required packages
RUN apt-get update && apt-get install -y curl ca-certificates tar sudo && update-ca-certificates

# Install Maven
RUN curl -fsSL https://downloads.apache.org/maven/maven-3/3.9.3/binaries/apache-maven-3.9.3-bin.tar.gz -o /tmp/maven.tar.gz \
    && tar -xzf /tmp/maven.tar.gz -C /opt \
    && ln -s /opt/apache-maven-3.9.3 /opt/maven

# Set Maven environment variables
ENV MAVEN_HOME=/opt/maven
ENV PATH="$MAVEN_HOME/bin:$PATH"

# Install Java 17
RUN apt-get install -y openjdk-17-jdk

WORKDIR /app

# Copy POM first
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source
COPY src ./src

CMD ["mvn", "test"]
