FROM adoptopenjdk/openjdk11
VOLUME /urlshortner
ARG JAR_FILE=target/Urlshortning-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} urlshortning.jar
EXPOSE 10095
ENTRYPOINT ["java", "-jar", "/urlshortning.jar"]
