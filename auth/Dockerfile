# Stage 1: Making jar file from project files
FROM maven:3.9.11-eclipse-temurin-21 AS stage1
# Set the working directory in the container
# WORKDIR /app
# Copy the pom.xml and the project files to the container
COPY pom.xml .
COPY src ./src
# Build the application using Maven
RUN mvn clean package -DskipTests
# Use an official OpenJDK image as the base image

FROM eclipse-temurin:21-jdk-alpine
#COPY /target/auth-0.0.1-SNAPSHOT.jar auth.jar
COPY --from=stage1 /target/auth-0.0.1-SNAPSHOT.jar /auth.jar

ENV MY_SECRET_KEY=U29tZXRoaW5nLXN0YXJ0cy1vbi10aW1lLWFuZC1uZXZlci1jb21lcy1iYWNr \
    JWT_EXPIRATION=86400000 \
    JWT_REFRESH_EXPIRATION=604800000

EXPOSE 8090
ENTRYPOINT ["java", "-jar", "auth.jar"]