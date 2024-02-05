FROM eclipse-temurin:21.0.2_13-jre-alpine
ARG JAR_FILE=build/docker/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]