FROM openjdk:11
RUN groupadd -r app-user && useradd -r -g app-user app-user
USER spring:spring
ARG JAR_FILE=build/libs/*.jar
WORKDIR /usr/app
COPY ${JAR_FILE} app.jar
EXPOSE 8082
USER app-user
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]