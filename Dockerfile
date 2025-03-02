# Build stage with JDK 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
# Copy all necessary files including resources
COPY pom.xml .
COPY src ./src
COPY .mvn .mvn
RUN mvn clean package -DskipTests


# Runtime stage with JRE 21
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Set timezone
ENV TZ=Europe/Moscow
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# Set JVM options
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]