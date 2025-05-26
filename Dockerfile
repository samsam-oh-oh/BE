# JDK 17 image start
FROM openjdk:17
# 인자 정리 - Jar
ARG JAR_FILE=build/libs/*.jar
# jar file copy
COPY ${JAR_FILE} aimockly.jar
ENTRYPOINT ["java", "-jar", "/almockly.jar"]