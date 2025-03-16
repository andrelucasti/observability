FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
COPY ./ ./
RUN mvn clean package -DskipTests

FROM eclipse-temurin:23
RUN mkdir /opt/app

COPY --from=build target/*.jar /opt/app/japp.jar

EXPOSE 8080

# Executa a aplicação com o Pyroscope Agent
CMD ["java", "-jar", "/opt/app/japp.jar"]
