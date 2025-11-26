FROM eclipse-temurin:21-jdk-alpine
COPY /target/userservice-0.0.1-SNAPSHOT.jar userservice.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "userservice.jar"]