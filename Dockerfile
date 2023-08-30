#podman build -t hello-world-java .
#Java 17 Maven Image Builder
FROM maven:3.8.1-openjdk-17-slim as builder

# Local code to container image
WORKDIR /app
COPY pom.xml .
COPY src ./src

#Build
RUN mvn clean package -DskipTests

#JDK Image for Runtime
FROM openjdk:17-jdk-slim-buster

# Copy Jar from builder stage
COPY --from=builder /app/target/application.jar /application.jar
COPY --from=builder /app/target/dependency-jars /dependency-jars

CMD ["java", "-jar", "/application.jar"]
