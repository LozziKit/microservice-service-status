FROM java:openjdk-8-jre

RUN apt-get install -y wget

ENV DOCKERIZE_VERSION v0.5.0
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-linux-amd64-$DOCKERIZE_VERSION.tar.gz

COPY ./target/microservice-service-status-1.0.0.jar server.jar

EXPOSE 8080

## Release config
CMD dockerize -wait tcp://database:3306 -timeout 1m && java -jar server.jar
