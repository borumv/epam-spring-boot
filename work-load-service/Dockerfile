FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/work-load-service-0.0.1-SNAPSHOT.jar /app/work-load-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "work-load-service.jar"]
