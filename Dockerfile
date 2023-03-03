FROM openjdk:11
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
EXPOSE 9000
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar"]
