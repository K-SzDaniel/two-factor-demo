FROM maven:3.9.9-eclipse-temurin-21-alpine AS BUILD
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean
RUN mvn package

FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]