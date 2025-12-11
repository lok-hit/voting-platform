FROM eclipse-temurin:17-jdk:17.0.9_7-jre
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} /app/app.jar
WORKDIR /app
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]