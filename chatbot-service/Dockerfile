FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*
WORKDIR /app
ENV SERVER_PORT=8091
COPY --from=build /app/target/chatbot-service-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8091
ENTRYPOINT ["java", "-jar", "app.jar"]