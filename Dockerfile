ARG VERSION=17
FROM openjdk:${VERSION} as BUILD

COPY . /src
WORKDIR /src
RUN ./gradle clean build

FROM openjdk:${VERSION}

COPY --from=BUILD /src/build/libs/services-0.0.1-SNAPSHOT.jar /app/services-0.0.1-SNAPSHOT.jar
WORKDIR /bin/runner

CMD ["java","-jar","/app/services-0.0.1-SNAPSHOT.jar"]