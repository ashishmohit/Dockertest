FROM selenium/standalone-chromium:latest

USER root

# Install dependencies for Maven
RUN apt-get update && apt-get install -y curl tar sudo

# Install Maven manually
RUN curl -o /tmp/apache-maven.tar.gz https://downloads.apache.org/maven/maven-3/3.9.3/binaries/apache-maven-3.9.3-bin.tar.gz \
    && tar -xzf /tmp/apache-maven.tar.gz -C /opt \
    && ln -s /opt/apache-maven-3.9.3 /opt/maven

# Setup Maven environment
ENV MAVEN_HOME=/opt/maven
ENV PATH="${MAVEN_HOME}/bin:${PATH}"

# Check Maven installation
RUN mvn -version

# Install JDK (required for Maven)
RUN apt-get install -y openjdk-17-jdk

WORKDIR /app

# Copy project files
COPY pom.xml .
RUN mvn dependency:resolve

COPY src ./src

CMD ["mvn", "test"]
