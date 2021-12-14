FROM openjdk:17-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} managment-app.jar
ENTRYPOINT ["java","-jar","/managment-app.jar"]