FROM openjdk:8

COPY target/spring-capstone.jar spring-capstone.jar

MAINTAINER capstone.com

#WORKDIR /app

ENTRYPOINT ["java", "-jar", "/spring-capstone.jar"]

EXPOSE 8080
