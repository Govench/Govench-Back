FROM openjdk:21-jdk-slim

ARG JAR_FILE=target/govench-api-0.0.1.jar

COPY ${JAR_FILE} govench-api.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","govench-api.jar"]
