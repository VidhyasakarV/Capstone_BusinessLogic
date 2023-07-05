FROM openjdk:8

#WORKDIR /app

COPY target/spring-capstone.jar spring-capstone.jar

EXPOSE 8080

MAINTAINER capstone.com

ENTRYPOINT ["java", "-jar", "/spring-capstone.jar"]