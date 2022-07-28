FROM openjdk:17
EXPOSE 8080
RUN mkdir -p /app/
COPY ./build/libs/services-0.0.1-SNAPSHOT.jar /app/services-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar", "/app/services-0.0.1-SNAPSHOT"]