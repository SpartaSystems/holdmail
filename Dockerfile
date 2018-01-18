FROM openjdk:8u92-alpine

ARG JAR_FILE
ARG CONF_FILE
ARG PROP_FILE
ARG MYSQL_CONNECTOR_VERSION=5.0.8

RUN apk add --update bash curl unzip && \
    rm -rf /var/cache/apk/*

# Install MySQL Connector
WORKDIR /opt/holdmail/lib
RUN curl -sLO https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-${MYSQL_CONNECTOR_VERSION}.zip && \
    unzip mysql-connector-java-${MYSQL_CONNECTOR_VERSION}.zip

# Install Holdmail

RUN mkdir -p /var/log/holdmail

WORKDIR /etc
ADD ${PROP_FILE} holdmail.properties

WORKDIR /opt/holdmail/bin/
ADD ${JAR_FILE} holdmail.jar
ADD ${CONF_FILE} holdmail.conf

EXPOSE 8080 25000

ENTRYPOINT ["./holdmail.jar"]
