# ---------- dev: chạy mvn spring-boot:run trực tiếp trong container ----------
FROM maven:3.9-eclipse-temurin-21 AS dev
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
EXPOSE 8080
CMD ["mvn", "-B", "spring-boot:run"]

# ---------- build: đóng gói jar ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /workspace
COPY pom.xml .
RUN mvn -B dependency:go-offline
COPY src ./src
RUN mvn -B -DskipTests package

# ---------- runtime: chỉ JRE + jar ----------
FROM eclipse-temurin:21-jre AS runtime
WORKDIR /app
RUN useradd -r -u 1001 spring
COPY --from=build /workspace/target/*.jar app.jar
USER spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]