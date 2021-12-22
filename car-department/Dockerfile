FROM adoptopenjdk:11-jre-hotspot
ARG JAR=build/libs/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "application.jar"]