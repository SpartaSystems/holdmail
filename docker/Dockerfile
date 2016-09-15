FROM openjdk:8u92-alpine

ARG HOLDMAIL_VERSION=1.0.0
ARG MYSQL_CONNECTOR_VERSION=5.0.8

RUN apk add --update bash curl unzip && \
    rm -rf /var/cache/apk/*

# Install MySQL Connector
WORKDIR /opt/holdmail/lib
RUN curl -sLO https://dev.mysql.com/get/Downloads/Connector-J/mysql-connector-java-${MYSQL_CONNECTOR_VERSION}.zip && \
    unzip mysql-connector-java-${MYSQL_CONNECTOR_VERSION}.zip

# Install Holdmail
WORKDIR /opt/holdmail/bin/
RUN mkdir -p /var/log/holdmail && \
    curl -sLo holdmail-${HOLDMAIL_VERSION}.jar https://bintray.com/spartasystems/HoldMail/download_file?file_path=com%2Fspartasystems%2Fholdmail%2Fholdmail%2F${HOLDMAIL_VERSION}%2Fholdmail-${HOLDMAIL_VERSION}.jar && \
    chmod +x holdmail-${HOLDMAIL_VERSION}.jar && \
    ln -s holdmail-${HOLDMAIL_VERSION}.jar holdmail.jar && \
    ls -l

COPY ./assets/etc/holdmail.properties /etc/holdmail.properties
COPY ./assets/opt/holdmail/bin/holdmail.conf /opt/holdmail/bin/holdmail.conf

EXPOSE 8080 25000

ENTRYPOINT ["./holdmail.jar"]
