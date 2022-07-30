FROM maven:3.8.5-amazoncorretto-11 AS build
COPY src /home/src
COPY pom.xml /home
RUN mvn -f /home/pom.xml clean package -Dmaven.test.skip=true

FROM amazoncorretto:11-alpine

COPY --from=build /home/target/*.jar action-monitor.jar
ENTRYPOINT ["java","-jar","/action-monitor.jar"]