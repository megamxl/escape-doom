# specify the base of our image
FROM maven:3-eclipse-temurin-17-alpine AS builder

# location of .jar file
WORKDIR /app

COPY . .
RUN mvn clean install -DskipTests

FROM openjdk:17-oracle

COPY --from=builder /app/target/*.jar /

EXPOSE 8090

ENTRYPOINT ["java","-jar","GameSession-0.0.1.jar"]
