FROM openjdk:8

#WORKDIR /app

COPY target/spring-capstone.jar spring-capstone.jar

EXPOSE 8080

CMD ["java", "-jar", "/spring-capstone.jar"]