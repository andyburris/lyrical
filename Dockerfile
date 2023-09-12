FROM gradle:7-jdk11 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle backend:buildFatJar --no-daemon

FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY --from=build /home/gradle/src/backend/build/libs/*.jar /app/lyrical-backend.jar
ENTRYPOINT ["java","-jar","/app/lyrical-backend.jar"]