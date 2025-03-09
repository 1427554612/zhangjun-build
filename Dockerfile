FROM openjdk:11
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
EXPOSE 20000
CMD ["java","-jar","/app.jar"]