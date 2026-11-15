FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY sistema-m01-m02/pom.xml sistema-m01-m02/pom.xml
COPY sistema-m01-m02/src sistema-m01-m02/src
RUN mvn -B -f sistema-m01-m02/pom.xml -Papi-only -DskipTests package

FROM eclipse-temurin:21-jre
RUN groupadd --system app && useradd --system --gid app --home-dir /app app
WORKDIR /app
COPY --from=build --chown=app:app /workspace/sistema-m01-m02/target/backend-0.1.0-SNAPSHOT.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java","-Xmx350m","-XX:MaxMetaspaceSize=128m","-XX:+UseSerialGC","-jar","/app/app.jar"]
