FROM java:openjdk-8-jre

COPY ./target/microservice-service-status-1.0.0.jar server.jar

## Release config
ENTRYPOINT ["java", "-jar", "server.jar"]
