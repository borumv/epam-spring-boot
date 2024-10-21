
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/report-service-0.0.1-SNAPSHOT.jar /app/report-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "report-service.jar"]