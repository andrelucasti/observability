FROM maven:3.9.9-eclipse-temurin-23-alpine AS build
COPY ./ ./
RUN mvn clean package -DskipTests

FROM eclipse-temurin:23
RUN mkdir /opt/app

ADD https://github.com/grafana/pyroscope-java/releases/latest/download/pyroscope.jar /opt/agents/pyroscope.jar
COPY --from=build target/*.jar /opt/app/japp.jar

EXPOSE 8080

# Executa a aplicação com o Pyroscope Agent
CMD ["java", "-javaagent:/opt/agents/pyroscope.jar", "-Dpyroscope.server.address=http://pyroscope.pyroscope.svc.cluster.local.:4040", "-Dpyroscope.application.name=toilet", "-jar", "/opt/app/japp.jar"]
