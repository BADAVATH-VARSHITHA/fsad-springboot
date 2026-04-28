# ---- Build Stage ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy pom.xml first for dependency caching
COPY fsad-springboot-main/springboot/pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY fsad-springboot-main/springboot/src ./src
RUN mvn clean package -DskipTests -B

# ---- Run Stage ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Railway injects PORT env variable
EXPOSE 8080

ENTRYPOINT ["java", "-Xmx512m", "-jar", "app.jar"]
