FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app

# Copy pom first so dependency layer is cached until pom.xml changes
COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
# Tests are skipped here: contextLoads needs a running MySQL. Run `./mvnw test` separately.
RUN mvn package -DskipTests -B

FROM eclipse-temurin:25-jre-alpine

# Run as a non-root user
RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

COPY --from=build --chown=app:app /app/target/coffeeshop-order-management-0.0.1-SNAPSHOT.jar app.jar

ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m"

EXPOSE 8080

USER app

ENTRYPOINT ["java", "-jar", "app.jar"]