FROM bellsoft/liberica-openjdk-alpine:17.0.2-9
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/covid-case-*.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]