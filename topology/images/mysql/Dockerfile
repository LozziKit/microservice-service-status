FROM mysql:5.7

ENV MYSQL_ROOT_PASSWORD root
ENV MYSQL_DATABASE microservice

COPY docker-entrypoint-initdb.d/quartz.sql docker-entrypoint-initdb.d/quartz.sql
