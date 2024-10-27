FROM maven:3-amazoncorretto-17-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM amazoncorretto:17
VOLUME /tmp
EXPOSE 8080

ARG JAR_FILE=/app/target/lcs_test-1.0-SNAPSHOT.jar
COPY --from=build ${JAR_FILE} app.jar

ENTRYPOINT ["java","-jar","/app.jar"]