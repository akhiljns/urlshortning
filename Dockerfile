FROM openjdk:8-jdk-alpine
MAINTAINER akhiljns
COPY target/Urlshortning-0.0.1-SNAPSHOT.jar urlshortning.jar
ENTRYPOINT ["java","-jar","/urlshortning.jar"]
